package com.sunny.microservices.course.service.lesson;

import com.sunny.microservices.basedomain.course.dto.DTO.LessonLearning;
import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.dto.DTO.LessonDetail;
import com.sunny.microservices.course.dto.DTO.LessonPreview;
import com.sunny.microservices.course.dto.response.lesson.ArticleResponse;
import com.sunny.microservices.course.dto.response.lesson.ExamResponse;
import com.sunny.microservices.course.dto.response.lesson.VideoResponse;
import com.sunny.microservices.course.entity.Lesson;
import com.sunny.microservices.course.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetLessonService {
    LessonRepository lessonRepository;
    VideoRepository videoRepository;
    ExamRepository examRepository;
    ArticleRepository articleRepository;
    ExamService examService;
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
}
