package com.sunny.microservices.course.service.lesson;

import com.sunny.microservices.basedomain.course.dto.event.LessonCreatedEvent;
import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.client.UserClient;
import com.sunny.microservices.course.dto.response.ProfileResponse;
import com.sunny.microservices.course.entity.Lesson;
import com.sunny.microservices.course.entity.Section;
import com.sunny.microservices.course.entity.Video;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.kafka.LessonProducer;
import com.sunny.microservices.course.repository.LessonRepository;
import com.sunny.microservices.course.repository.SectionRepository;
import com.sunny.microservices.course.repository.VideoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonProcessingService {
    VideoRepository videoRepository;
    LessonRepository lessonRepository;
    SectionRepository sectionRepository;
    MongoTemplate mongoTemplate;
    AzureFileStorageClient azureFileStorageClient;
    UserClient userClient;
    LessonProducer lessonProducer;
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
    public void processCreateVideoLesson(String userId, String sectionId,
                                         String lessonName,
                                         Integer partNumber, Double duration,
                                         String videoName, byte[] videoFile,
                                         String thumbnailName, byte[] thumbnailFile) {
        ProfileResponse user = userClient.getProfile(userId);
        Section section = sectionRepository.findById(sectionId).orElseThrow(
                () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
        );

        Lesson lesson = Lesson.builder()
                .name(lessonName)
                .type("VIDEO")
                .partNumber(partNumber)
                .comments(new ArrayList<>()).build();

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
        section.setDuration(newDuraton);

        sectionRepository.save(section);

        log.info("success create lesson");
        LessonCreatedEvent event = new LessonCreatedEvent(lesson.getName(), user.getUsername(), user.getEmail());

        lessonProducer.sendMessage(event);

    }
}
