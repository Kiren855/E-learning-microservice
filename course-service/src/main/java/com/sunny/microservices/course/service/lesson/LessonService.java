package com.sunny.microservices.course.service.lesson;


import com.sunny.microservices.basedomain.course.dto.DTO.LessonLearning;
import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.dto.DTO.LessonDetail;
import com.sunny.microservices.course.dto.DTO.LessonPreview;
import com.sunny.microservices.course.dto.request.lesson.*;
import com.sunny.microservices.course.dto.response.lesson.ArticleResponse;
import com.sunny.microservices.course.dto.response.lesson.ExamResponse;
import com.sunny.microservices.course.dto.response.lesson.VideoResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Comparator;
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

    public String updateLesson(String lessonId, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        lesson.setName(request.getName());
        lesson.setPartNumber(request.getPartNumber());

        return "Cập nhật bài giảng thành công";
    }
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

        section.getLessons().removeIf(existingLessonId -> existingLessonId.equals(lessonId));
        lessonRepository.delete(lesson);
        sectionRepository.save(section);

        return "xoá bài học thành công";
    }
    public String createVideoLesson(String sectionId, VideoLessonRequest request) {
       try {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String userId = authentication.getName();

           String nameLesson = request.getName();
           Integer partNumber = request.getPartNumber();
           Double duration = request.getDuration();
           MultipartFile videoFile = request.getVideoFile();
           MultipartFile thumbnailFile = request.getThumbnailFile();
           String videoName = videoFile.getOriginalFilename();
           String thumbnailName = thumbnailFile.getOriginalFilename();

           lessonProcessingService.processCreateVideoLesson(userId, sectionId, nameLesson, partNumber, duration,
                   videoName ,IOUtils.toByteArray(videoFile.getInputStream()),
                   thumbnailName, IOUtils.toByteArray(thumbnailFile.getInputStream()));

           return "Hệ thống đã nhận được yêu cầu và đang tiến hành upload bài học này";
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

    public String createExam(String sectionId, ExamRequest request) {
        Section section = sectionRepository.findById(sectionId).orElseThrow(
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
                .contents(examService.mapQuestionRequestToEntity(request.getContents())).build();

        examRepository.save(exam);

        lesson.setType_id(exam.getId());
        lessonRepository.save(lesson);

        section.getLessons().add(lesson.getId());
        sectionRepository.save(section);

        return "Tạo thành công bài kiểm tra";
    }

    public String updateExam(String lessonId, ExamRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new AppException(ErrorCode.LESSON_NOT_FOUND));

        if(Objects.equals(lesson.getType(), "EXAM")) {
                String examId = lesson.getType_id();

                Exam exam = examRepository.findById(examId)
                        .orElseThrow(()-> new AppException(ErrorCode.DOC_NOT_FOUND));

                exam.setContents(examService.mapQuestionRequestToEntity(request.getContents()));
                examRepository.save(exam);
        }
        return "Cập nhật bài kiểm tra thành công";
    }

    public List<Lesson> findLessonsByIds(List<String> lessonIds) {
        return lessonRepository.findAllById(lessonIds);
    }

    public List<LessonPreview> mapToLessonPreviews(List<Lesson> lessons) {
        return lessons.stream()
                .sorted(Comparator.comparingInt(Lesson::getPartNumber))
                .map(lesson -> {
                    LessonPreview.LessonPreviewBuilder builder = LessonPreview.builder()
                            .id(lesson.getId())
                            .name(lesson.getName())
                            .type(lesson.getType())
                            .type_id(lesson.getType_id());

                    if ("VIDEO".equals(lesson.getType())) {
                        videoRepository.findById(lesson.getType_id()).ifPresent(video -> builder.duration(video.getDuration()));
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());
    }

    public List<LessonLearning> mapToLessonLearnings(List<Lesson> lessons) {
        return lessons.stream()
                .sorted(Comparator.comparingInt(Lesson::getPartNumber))
                .map(lesson -> {
                    LessonLearning.LessonLearningBuilder builder = LessonLearning.builder()
                            .id(lesson.getId())
                            .name(lesson.getName())
                            .type(lesson.getType())
                            .type_id(lesson.getType_id())
                            .partNumber(lesson.getPartNumber());

                    if ("VIDEO".equals(lesson.getType())) {
                        videoRepository.findById(lesson.getType_id()).ifPresent(video -> builder.duration(video.getDuration()));
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());
    }

    public List<LessonDetail> mapToLessonDetails(List<Lesson> lessons) {
        return lessons.stream()
                .sorted(Comparator.comparingInt(Lesson::getPartNumber))
                .map(lesson -> {
                    LessonDetail.LessonDetailBuilder builder = LessonDetail.builder()
                            .id(lesson.getId())
                            .name(lesson.getName())
                            .type(lesson.getType())
                            .type_id(lesson.getType_id())
                            .partNumber(lesson.getPartNumber())
                            ;
                    if ("VIDEO".equals(lesson.getType())) {
                        videoRepository.findById(lesson.getType_id()).ifPresent(video -> {
                            VideoResponse videoResponse = VideoResponse.builder()
                                    .id(video.getId())
                                    .duration(video.getDuration())
                                    .videoUrl(video.getVideoUrl())
                                    .thumbnailUrl(video.getThumbnailUrl()).build();
                            builder.video(videoResponse);
                        });
                    }
                    else if ("EXAM".equals(lesson.getType())) {
                        examRepository.findById(lesson.getType_id()).ifPresent(exam -> {
                            ExamResponse examResponse = ExamResponse.builder()
                                    .id(exam.getId())
                                    .title(exam.getTitle())
                                    .subTitle(exam.getSubTitle())
                                    .contents(examService.mapQuestionEntityToResponse(exam.getContents())).build();
                            builder.exam(examResponse);
                        });
                    } else if ("ARTICLE".equals(lesson.getType())) {
                        articleRepository.findById(lesson.getType_id()).ifPresent(article -> {
                            ArticleResponse articleResponse = ArticleResponse.builder()
                                    .id(article.getId())
                                    .content(article.getContent()).build();
                            builder.article(articleResponse);
                        });
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());
    }

    private String extractFileName(String blobUrl) {
        return blobUrl.substring(blobUrl.lastIndexOf("/") + 1);
    }

    public String createArticle(String sectionId, ArticleLessonRequest request) {
        Section section = sectionRepository.findById(sectionId).orElseThrow(
                () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
        );
        Lesson lesson = Lesson.builder()
                .name(request.getName())
                .type("ARTICLE")
                .partNumber(request.getPartNumber())
                .build();

        Article article = Article.builder()
               .content(request.getContent()).build();

        articleRepository.save(article);
        lesson.setType_id(article.getId());
        lessonRepository.save(lesson);
        section.getLessons().add(lesson.getId());
        sectionRepository.save(section);

        return "Tạo bài viết thành công";
    }

    public String updateArticle(String lessonId, String content) {
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(()-> new AppException(ErrorCode.LESSON_NOT_FOUND));

            String articleId = lesson.getType_id();
            if(Objects.equals(lesson.getType(), "ARTICLE")) {
                    Article article = articleRepository.findById(articleId)
                            .orElseThrow(()-> new AppException(ErrorCode.DOC_NOT_FOUND));

                    article.setContent(content);
                    articleRepository.save(article);
            }
            return "Cập nhật bài viết thành công";
    }

}
