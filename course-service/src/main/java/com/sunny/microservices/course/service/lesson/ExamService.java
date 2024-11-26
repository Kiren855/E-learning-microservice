package com.sunny.microservices.course.service.lesson;

import com.sunny.microservices.course.dto.request.lesson.ExamRequest;
import com.sunny.microservices.course.dto.response.lesson.ExamResponse;
import com.sunny.microservices.course.entity.Exam;
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
public class ExamService {
    public List<ExamResponse.QuestionResponse> mapQuestionEntityToResponse(List<Exam.Question> questions) {
        return questions.stream().map(qr ->
                ExamResponse.QuestionResponse.builder()
                        .question(qr.getQuestion())
                        .options(mapOptionEntityToResponse(qr.getOptions())).build()
        ).collect(Collectors.toList());
    }

    private List<ExamResponse.OptionResponse> mapOptionEntityToResponse(List<Exam.Option> options) {
        return options.stream().map(or ->
                ExamResponse.OptionResponse.builder()
                        .optionText(or.getOptionText())
                        .isCorrect(or.getIsCorrect()).build()
        ).collect(Collectors.toList());
    }

    public List<Exam.Question> mapQuestionRequestToEntity(List<ExamRequest.QuestionRequest> questionRequests) {
        return questionRequests.stream().map(qr ->
                Exam.Question.builder()
                        .question(qr.getQuestion())
                        .options(mapOptionRequestToEntity(qr.getOptions()))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<Exam.Option> mapOptionRequestToEntity(List<ExamRequest.OptionRequest> optionRequests) {
        return optionRequests.stream().map(or ->
                Exam.Option.builder()
                        .optionText(or.getOptionText())
                        .isCorrect(or.getIsCorrect())
                        .build()
        ).collect(Collectors.toList());
    }
}
