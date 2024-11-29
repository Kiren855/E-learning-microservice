package com.sunny.microservices.course.service.lesson;

import com.sunny.microservices.course.dto.response.lesson.ArticleResponse;
import com.sunny.microservices.course.dto.response.lesson.ExamResponse;
import com.sunny.microservices.course.dto.response.lesson.VideoResponse;
import com.sunny.microservices.course.entity.Article;
import com.sunny.microservices.course.entity.Exam;
import com.sunny.microservices.course.entity.Video;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.ArticleRepository;
import com.sunny.microservices.course.repository.ExamRepository;
import com.sunny.microservices.course.repository.VideoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class  SubLessonService {
    VideoRepository videoRepository;
    ExamRepository examRepository;
    ArticleRepository articleRepository;
    ExamService examService;

    public VideoResponse getVideoLesson(String typeId) {
        Video video = videoRepository.findById(typeId)
                .orElseThrow(()-> new AppException(ErrorCode.DOC_NOT_FOUND));

        return VideoResponse.builder()
                .id(video.getId())
                .videoUrl(video.getVideoUrl())
                .thumbnailUrl(video.getThumbnailUrl())
                .duration(video.getDuration()).build();
    }

    public ArticleResponse getArticle(String typeId) {
        Article article = articleRepository.findById(typeId)
                .orElseThrow(()-> new AppException(ErrorCode.DOC_NOT_FOUND));

        return ArticleResponse.builder()
                .id(article.getId())
                .content(article.getContent()).build();
    }

    public ExamResponse getExam(String typeId) {
        Exam exam = examRepository.findById(typeId)
                .orElseThrow(()-> new AppException(ErrorCode.DOC_NOT_FOUND));

        return ExamResponse.builder()
                .id(exam.getId())
                .title(exam.getTitle())
                .subTitle(exam.getSubTitle())
                .contents(examService.mapQuestionEntityToResponse(exam.getContents())).build();
    }


}
