package com.sunny.microservices.course.service.lesson;


import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.dto.request.lesson.*;
import com.sunny.microservices.course.dto.response.IdResponse;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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
    ExamRepository examRepository;
    LessonProcessingService lessonProcessingService;
    ArticleRepository articleRepository;
    ExamService examService;
    @Value("${azure.blob.video-container}")
    @NonFinal
    String videoContainer;

    @Value("${azure.blob.doc-container}")
    @NonFinal
    String docContainer;

    @Value("${azure.blob.thumbnail-container}")
    @NonFinal
    String thumbnailContainer;

    @CacheEvict(value = {"course-client"}, allEntries = true)
    public String updateLesson(String lessonId, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        if(request.getName() != null)
            lesson.setName(request.getName());

        if(request.getPartNumber() != null)
            lesson.setPartNumber(request.getPartNumber());

        lessonRepository.save(lesson);

        return "Cập nhật bài giảng thành công";
    }
    @CacheEvict(value = {"course-client"}, allEntries = true)
    public String deleteLesson(String sectionId,String lessonId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new AppException(ErrorCode.SECTION_NOT_FOUND));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        String type = lesson.getType();

        switch (type) {
            case "VIDEO" -> {
                Video video = videoRepository.findById(lesson.getType_id())
                        .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));

                String videoFileUrl = extractFileName(video.getVideoUrl());
                String thumbnailUrl = extractFileName(video.getThumbnailUrl());
                azureFileStorageClient.deleteFile(videoContainer, videoFileUrl);
                azureFileStorageClient.deleteFile(thumbnailContainer, thumbnailUrl);

                section.setDuration(section.getDuration() - video.getDuration());
                videoRepository.delete(video);
            }
            case "DOC" -> {
                Doc doc = docRepository.findById(lesson.getType_id())
                        .orElseThrow(() -> new AppException(ErrorCode.DOC_NOT_FOUND));

                String docFileUrl = extractFileName(doc.getFileUrl());
                azureFileStorageClient.deleteFile(docContainer, docFileUrl);

                docRepository.delete(doc);
            }
            case "EXAM" -> {
                Exam exam = examRepository.findById(lesson.getType_id())
                        .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

                examRepository.delete(exam);
            }
            case "ARTICLE" -> {
                Article article = articleRepository.findById(lesson.getType_id())
                        .orElseThrow(()-> new AppException(ErrorCode.DOC_NOT_FOUND));

                articleRepository.delete(article);
            }
            case null, default -> {
                return "có gì lạ lạ đang xảy ra";
            }
        }

        section.getLessons().removeIf(c-> c.equals(lessonId));
        section.setTotalLesson(section.getTotalLesson() - 1);
        lessonRepository.delete(lesson);
        sectionRepository.save(section);

        return "xoá bài học thành công";
    }
    @CacheEvict(value = {"course-client"}, allEntries = true)
    public IdResponse createVideoLesson(String sectionId, VideoLessonRequest request) {
       try {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String userId = authentication.getName();
           Section section = sectionRepository.findById(sectionId).orElseThrow(
                   () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
           );

           String nameLesson = request.getName();
           Double duration = request.getDuration();
           MultipartFile videoFile = request.getVideoFile();
           MultipartFile thumbnailFile = request.getThumbnailFile();
           String videoName = videoFile.getOriginalFilename();
           String thumbnailName = thumbnailFile.getOriginalFilename();
           Integer partNumber =  section.getTotalLesson() + 1;

           if(request.getPartNumber() != null)
                partNumber = request.getPartNumber();

           Lesson lesson = Lesson.builder()
                   .name(nameLesson)
                   .type("VIDEO")
                   .partNumber(partNumber)
                   .comments(new ArrayList<>()).build();

           lessonProcessingService.processCreateVideoLesson(userId, section, lesson, duration,
                   videoName ,IOUtils.toByteArray(videoFile.getInputStream()),
                   thumbnailName, IOUtils.toByteArray(thumbnailFile.getInputStream()));

           return IdResponse.builder().Id(lesson.getId()).build();
       }catch (IOException e) {
           throw new AppException(ErrorCode.FILE_INVALID);
       }
    }

    public String updateVideoLesson(String sectionId, String lessonId, VideoLessonRequest request) {
        try {
            Section section = sectionRepository.findById(sectionId)
                    .orElseThrow(()-> new AppException(ErrorCode.SECTION_NOT_FOUND));
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(()-> new AppException(ErrorCode.LESSON_NOT_FOUND));

            if(request.getName() != null) {
                lesson.setName(request.getName());
            }
            if (request.getPartNumber() != null)
                lesson.setPartNumber(request.getPartNumber());

            lessonRepository.save(lesson);
            //////////////////////////////////////////////////////////
            if(Objects.equals(lesson.getType(), "VIDEO")) {
                String videoId = lesson.getType_id();
                Video video = videoRepository.findById(videoId)
                        .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));

                if(request.getVideoFile() != null ){
                    Double duration = request.getDuration();
                    MultipartFile videoFile = request.getVideoFile();
                    String videoName = videoFile.getOriginalFilename();
                    lessonProcessingService.processUpdateVideo(section, video, videoName,
                            IOUtils.toByteArray(videoFile.getInputStream()), duration);
                }

                if(request.getThumbnailFile() != null) {
                    MultipartFile thumbnailFile = request.getThumbnailFile();
                    String thumbnailName = thumbnailFile.getOriginalFilename();
                    lessonProcessingService.processUpdateThumbnail(video, thumbnailName,  IOUtils.toByteArray(thumbnailFile.getInputStream()));
                }
            }
            return "cập nhật video thành công";
        }catch (IOException e) {
            throw new AppException(ErrorCode.FILE_INVALID);
        }
    }

    public String createDocLesson(String sectionId, DocLessonRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();

            String nameLesson = request.getName();
            Integer partNumber = request.getPartNumber();

            MultipartFile docFile = request.getDocFile();
            String docName = docFile.getOriginalFilename();

            lessonProcessingService.processCreateDocLesson(userId, sectionId, nameLesson,
                    partNumber, docName, IOUtils.toByteArray(docFile.getInputStream()));

            return "Hệ thống đã nhận được yêu cầu và đang tiến hành upload bài học này";
        }catch (IOException e) {
            throw new AppException(ErrorCode.FILE_INVALID);
        }
    }

    @CacheEvict(value = {"course-client"}, allEntries = true)
    public IdResponse createExam(String sectionId, ExamRequest request) {
        Section section = sectionRepository.findById(sectionId).orElseThrow(
                () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
        );

        Integer partNumber = section.getTotalLesson() + 1;
        if(request.getPartNumber() != null)
            partNumber = request.getPartNumber();

        Lesson lesson = Lesson.builder()
                .name(request.getName())
                .type("EXAM")
                .partNumber(partNumber)
                .build();

        Exam exam = Exam.builder()
                .title(request.getTitle())
                .subTitle(request.getSubTitle())
                .contents(examService.mapQuestionRequestToEntity(request.getContents())).build();

        examRepository.save(exam);

        lesson.setType_id(exam.getId());
        lessonRepository.save(lesson);

        section.getLessons().add(lesson.getId());
        section.setTotalLesson(section.getTotalLesson() + 1);
        sectionRepository.save(section);

        return IdResponse.builder().Id(lesson.getId()).build();
    }

    public String updateExam(String lessonId, ExamRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new AppException(ErrorCode.LESSON_NOT_FOUND));

        if(request.getName() != null)
            lesson.setName(request.getName());
        if(request.getPartNumber() != null)
            lesson.setPartNumber(request.getPartNumber());
        lessonRepository.save(lesson);

        if(Objects.equals(lesson.getType(), "EXAM")) {
                String examId = lesson.getType_id();

                Exam exam = examRepository.findById(examId)
                        .orElseThrow(()-> new AppException(ErrorCode.DOC_NOT_FOUND));

                if(request.getTitle() != null)
                    exam.setTitle(request.getTitle());

                if(request.getSubTitle() != null)
                    exam.setSubTitle(request.getSubTitle());

                if(request.getContents() != null)
                    exam.setContents(examService.mapQuestionRequestToEntity(request.getContents()));
                examRepository.save(exam);
        }
        return "Cập nhật bài kiểm tra thành công";
    }


    private String extractFileName(String blobUrl) {
        return blobUrl.substring(blobUrl.lastIndexOf("/") + 1);
    }

    @CacheEvict(value = {"course-client"}, allEntries = true)
    public IdResponse createArticle(String sectionId, ArticleLessonRequest request) {
        Section section = sectionRepository.findById(sectionId).orElseThrow(
                () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
        );

        Integer partNumber = section.getTotalLesson() + 1;
        if(request.getPartNumber() != null)
            partNumber = request.getPartNumber();

        Lesson lesson = Lesson.builder()
                .name(request.getName())
                .type("ARTICLE")
                .partNumber(partNumber)
                .build();

        Article article = Article.builder()
               .content(request.getContent()).build();

        articleRepository.save(article);
        lesson.setType_id(article.getId());
        lessonRepository.save(lesson);
        section.getLessons().add(lesson.getId());
        section.setTotalLesson(section.getTotalLesson() + 1);
        sectionRepository.save(section);

        return IdResponse.builder().Id(lesson.getId()).build();
    }

    public String updateArticle(String lessonId, ArticleLessonRequest request) {
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(()-> new AppException(ErrorCode.LESSON_NOT_FOUND));

            if(request.getName() != null)
                lesson.setName(request.getName());
            if(request.getPartNumber() != null)
                lesson.setPartNumber(request.getPartNumber());
            lessonRepository.save(lesson);

            String articleId = lesson.getType_id();
            if(Objects.equals(lesson.getType(), "ARTICLE")) {
                    Article article = articleRepository.findById(articleId)
                            .orElseThrow(()-> new AppException(ErrorCode.DOC_NOT_FOUND));

                    if(request.getContent() != null) {
                        article.setContent(request.getContent());
                        articleRepository.save(article);
                    }
            }
            return "Cập nhật bài viết thành công";
    }

}
