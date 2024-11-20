package com.sunny.microservices.course.service;


import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.dto.DTO.LessonPreview;
import com.sunny.microservices.course.dto.request.DocLessonRequest;
import com.sunny.microservices.course.dto.request.ExamRequest;
import com.sunny.microservices.course.dto.request.VideoLessonRequest;
import com.sunny.microservices.course.entity.*;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonService {
    LessonRepository lessonRepository;
    VideoRepository videoRepository;
    SectionRepository sectionRepository;
    DocRepository docRepository;
    AzureFileStorageClient azureFileStorageClient;
    MongoTemplate mongoTemplate;
    ExamRepository examRepository;
    @Value("${azure.blob.video-container}")
    @NonFinal
    String videoContainer;

    @Value("${azure.blob.doc-container}")
    @NonFinal
    String docContainer;

    @Value("${azure.blob.thumbnail-container}")
    @NonFinal
    String thumbnailContainer;

    public String createVideoLesson(String sectionId, VideoLessonRequest request)  {
        try {
            Section section = sectionRepository.findById(sectionId).orElseThrow(
                    () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
            );

            Lesson lesson = Lesson.builder()
                    .name(request.getName())
                    .type("VIDEO")
                    .partNumber(request.getPartNumber())
                    .comments(new ArrayList<>()).build();

            String pathVideo = azureFileStorageClient.uploadFile(videoContainer,
                    Objects.requireNonNull(request.getVideoFile().getOriginalFilename()),
                    request.getVideoFile().getInputStream(),
                    request.getVideoFile().getSize());

            log.info("path video {}", pathVideo);

            String pathThumbnail = azureFileStorageClient.uploadFile(thumbnailContainer,
                    Objects.requireNonNull(request.getThumbnailFile().getOriginalFilename()),
                    request.getThumbnailFile().getInputStream(),
                    request.getThumbnailFile().getSize());

            Video video = Video.builder()
                    .videoUrl(pathVideo)
                    .duration(request.getDuration())
                    .thumbnailUrl(pathThumbnail)
                    .build();

            videoRepository.save(video);
            lesson.setType_id(video.getId());
            lessonRepository.save(lesson);

            Double newDuraton = section.getDuration() + request.getDuration();
            mongoTemplate.updateFirst(
                    new Query(Criteria.where("_id").is(sectionId)),
                    new Update()
                            .push("lessons", lesson.getId())
                            .set("duration", newDuraton),
                    Section.class
            );

            return "tạo bài học thành công";
        }catch (IOException e) {
            throw new AppException(ErrorCode.FILE_INVALID);
        }
    }

    public String createDocLesson(String sectionId, DocLessonRequest request) {
        try {
            sectionRepository.findById(sectionId).orElseThrow(
                    () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
            );

            Lesson lesson = Lesson.builder()
                    .name(request.getName())
                    .type("DOC")
                    .partNumber(request.getPartNumber()).build();

            String pathFile = azureFileStorageClient.uploadFile(docContainer, Objects.requireNonNull(request.getDocFile().getOriginalFilename()), request.getDocFile().getInputStream(), request.getDocFile().getSize());

            Doc doc = Doc.builder()
                    .fileUrl(pathFile).build();
            docRepository.save(doc);
            lesson.setType_id(doc.getId());
            lessonRepository.save(lesson);

            mongoTemplate.updateFirst(
                    new Query(Criteria.where("_id").is(sectionId)),
                    new Update().push("lessons", lesson.getId()),
                    Section.class
            );

            return "tạo bài học thành công";
        }catch (IOException e) {
            throw new AppException(ErrorCode.FILE_INVALID);
        }
    }

    public String createExam(String sectionId, ExamRequest request) {
        sectionRepository.findById(sectionId).orElseThrow(
                () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
        );

        Lesson lesson = Lesson.builder()
                .name(request.getName())
                .type("EXAM")
                .partNumber(request.getPartNumber())
                .build();

        Exam exam = Exam.builder()
                .title(request.getTitle())
                .subTitle(request.getSubTitle())
                .contents(mapQuestions(request.getContents())).build();

        examRepository.save(exam);

        lesson.setType_id(exam.getId());
        lessonRepository.save(lesson);

        mongoTemplate.updateFirst(
                new Query(Criteria.where("_id").is(sectionId)),
                new Update().push("lessons", lesson.getId()),
                Section.class
        );

        return "Tạo thành công bài kiểm tra";
    }

    private List<Exam.Question> mapQuestions(List<ExamRequest.QuestionRequest> questionRequests) {
        return questionRequests.stream().map(qr ->
                Exam.Question.builder()
                        .question(qr.getQuestion())
                        .options(mapOptions(qr.getOptions()))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<Exam.Option> mapOptions(List<ExamRequest.OptionRequest> optionRequests) {
        return optionRequests.stream().map(or ->
                Exam.Option.builder()
                        .optionText(or.getOptionText())
                        .isCorrect(or.getIsCorrect())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<Lesson> findLessonsByIds(List<String> lessonIds) {
        return lessonRepository.findAllById(lessonIds);
    }

    public List<LessonPreview> mapToLessonPreviews(List<Lesson> lessons) {
        return lessons.stream()
                .map(lesson -> {
                    LessonPreview.LessonPreviewBuilder builder = LessonPreview.builder()
                            .id(lesson.getId())
                            .name(lesson.getName())
                            .type(lesson.getType())
                            .type_id(lesson.getType_id())
                            .partNumber(lesson.getPartNumber());

                    if ("VIDEO".equals(lesson.getType())) {
                        videoRepository.findById(lesson.getType_id()).ifPresent(video -> {
                            builder.duration(video.getDuration());
                        });
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());
    }

}
