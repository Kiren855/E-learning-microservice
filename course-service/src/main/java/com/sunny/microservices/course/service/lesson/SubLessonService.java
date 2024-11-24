package com.sunny.microservices.course.service.lesson;

import com.sunny.microservices.course.dto.request.lesson.ExamRequest;
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
import com.sunny.microservices.course.repository.LessonRepository;
import com.sunny.microservices.course.repository.VideoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubLessonService {
    LessonRepository lessonRepository;
    VideoRepository videoRepository;
    ExamRepository examRepository;
    ArticleRepository articleRepository;

    public VideoResponse getVideoLesson(String typeId) {
        Video video = videoRepository.findById(typeId)
                .orElseThrow(()-> new AppException(ErrorCode.DOC_NOT_FOUND));

        return VideoResponse.builder()
                .videoUrl(video.getVideoUrl())
                .thumbnailUrl(video.getThumbnailUrl()).build();
    }

    public ArticleResponse getArticle(String typeId) {
        Article article = articleRepository.findById(typeId)
                .orElseThrow(()-> new AppException(ErrorCode.DOC_NOT_FOUND));

        return ArticleResponse.builder()
                .content(article.getContent()).build();
    }

    public ExamResponse getExam(String typeId) {
        Exam exam = examRepository.findById(typeId)
                .orElseThrow(()-> new AppException(ErrorCode.DOC_NOT_FOUND));

        return ExamResponse.builder()
                .title(exam.getTitle())
                .subTitle(exam.getSubTitle())
                .contents(mapQuestions(exam.getContents())).build();
    }

    private List<ExamResponse.QuestionResponse> mapQuestions(List<Exam.Question> questionRequests) {
        return questionRequests.stream().map(qr ->
                ExamResponse.QuestionResponse.builder()
                        .question(qr.getQuestion())
                        .options(mapOptions(qr.getOptions())).build()
        ).collect(Collectors.toList());
    }

    private List<ExamResponse.OptionResponse> mapOptions(List<Exam.Option> optionRequests) {
        return optionRequests.stream().map(or ->
                ExamResponse.OptionResponse.builder()
                        .optionText(or.getOptionText())
                        .isCorrect(or.getIsCorrect()).build()
        ).collect(Collectors.toList());
    }
}
