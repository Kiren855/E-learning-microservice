package com.sunny.microservices.course.service.lesson;

import com.sunny.microservices.basedomain.event.LessonCreatedEvent;
import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.client.UserClient;
import com.sunny.microservices.course.dto.response.ProfileResponse;
import com.sunny.microservices.course.entity.Doc;
import com.sunny.microservices.course.entity.Lesson;
import com.sunny.microservices.course.entity.Section;
import com.sunny.microservices.course.entity.Video;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.kafka.LessonProducer;
import com.sunny.microservices.course.repository.DocRepository;
import com.sunny.microservices.course.repository.LessonRepository;
import com.sunny.microservices.course.repository.SectionRepository;
import com.sunny.microservices.course.repository.VideoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonProcessingService {
    VideoRepository videoRepository;
    LessonRepository lessonRepository;
    SectionRepository sectionRepository;
    AzureFileStorageClient azureFileStorageClient;
    UserClient userClient;
    LessonProducer lessonProducer;
    DocRepository docRepository;
    @Value("${azure.blob.video-container}")
    @NonFinal
    String videoContainer;

    @Value("${azure.blob.doc-container}")
    @NonFinal
    String docContainer;

    @Value("${azure.blob.thumbnail-container}")
    @NonFinal
    String thumbnailContainer;
    @Async("threadPoolTaskExecutor")
    public void processCreateVideoLesson(String userId, Section section, Lesson lesson,
                                         Double duration,
                                         String videoName, byte[] videoFile,
                                         String thumbnailName, byte[] thumbnailFile) {
        ProfileResponse user = userClient.getProfile(userId);

        String pathVideo = azureFileStorageClient.uploadFile(videoContainer,
                videoName,
                new ByteArrayInputStream(videoFile),
                videoFile.length);

        String pathThumbnail = azureFileStorageClient.uploadFile(thumbnailContainer,
                thumbnailName,
                new ByteArrayInputStream(thumbnailFile),
                thumbnailFile.length);

        Video video = Video.builder()
                .videoUrl(pathVideo)
                .duration(duration)
                .thumbnailUrl(pathThumbnail)
                .build();

        videoRepository.save(video);
        lesson.setType_id(video.getId());
        lessonRepository.save(lesson);

        Double newDuraton = section.getDuration() + duration;

        section.getLessons().add(lesson.getId());
        section.setTotalLesson(section.getTotalLesson() + 1);
        section.setDuration(newDuraton);

        sectionRepository.save(section);

        log.info("success create video lesson");
        LessonCreatedEvent event = new LessonCreatedEvent(lesson.getName(), user.getUsername(), user.getEmail());

        lessonProducer.sendMessage(event);

    }
    @Async("threadPoolTaskExecutor")
    public void processCreateDocLesson(String userId, String sectionId,
                                       String lessonName, Integer partNumber,
                                       String docName, byte[] docFile) {

        ProfileResponse user = userClient.getProfile(userId);
        Section section = sectionRepository.findById(sectionId).orElseThrow(
                () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
        );

        Lesson lesson = Lesson.builder()
                .name(lessonName)
                .type("DOC")
                .partNumber(partNumber).build();

        String pathFile = azureFileStorageClient.uploadFile(docContainer, docName,
                new ByteArrayInputStream(docFile), docFile.length);

        Doc doc = Doc.builder()
                .fileUrl(pathFile).build();
        docRepository.save(doc);
        lesson.setType_id(doc.getId());
        lessonRepository.save(lesson);
        section.getLessons().add(lesson.getId());
        section.setTotalLesson(section.getTotalLesson() + 1);
        sectionRepository.save(section);

        log.info("success create doc lesson");
        LessonCreatedEvent event = new LessonCreatedEvent(lesson.getName(), user.getUsername(), user.getEmail());

        lessonProducer.sendMessage(event);
    }

}
