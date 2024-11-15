package com.sunny.microservices.course.service;


import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.dto.request.ExamRequest;
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

    public String createVideoLesson(String sectionId, String name, String type, Integer partNumber, MultipartFile videoFile, MultipartFile thumbnailFile, Double duration)  {
        try {
            Section section = sectionRepository.findById(sectionId).orElseThrow(
                    () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
            );

            Lesson lesson = Lesson.builder()
                    .name(name)
                    .type(type)
                    .partNumber(partNumber)
                    .comments(new ArrayList<>()).build();

            String pathVideo = azureFileStorageClient.uploadFile(videoContainer, Objects.requireNonNull(videoFile.getOriginalFilename()), videoFile.getInputStream(), videoFile.getSize());
            String pathThumbnail = azureFileStorageClient.uploadFile(thumbnailContainer, Objects.requireNonNull(thumbnailFile.getOriginalFilename()), thumbnailFile.getInputStream(), thumbnailFile.getSize());
            Video video = Video.builder()
                    .videoUrl(pathVideo)
                    .duration(duration)
                    .thumbnailUrl(pathThumbnail)
                    .build();

            videoRepository.save(video);
            lesson.setType_id(video.getId());
            lessonRepository.save(lesson);

            Double newDuraton = section.getDuration() + duration;
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

    public String createDocLesson(String sectionId, String name, String type, Integer partNumber, MultipartFile docFile) {
        try {
            sectionRepository.findById(sectionId).orElseThrow(
                    () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
            );

            Lesson lesson = Lesson.builder()
                    .name(name)
                    .type(type)
                    .partNumber(partNumber)
                    .comments(new ArrayList<>()).build();

            String pathFile = azureFileStorageClient.uploadFile(docContainer, Objects.requireNonNull(docFile.getOriginalFilename()), docFile.getInputStream(), docFile.getSize());

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
                .comments(new ArrayList<>()).build();

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
}
