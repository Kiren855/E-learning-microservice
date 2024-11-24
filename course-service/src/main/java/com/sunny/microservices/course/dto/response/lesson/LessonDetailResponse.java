package com.sunny.microservices.course.dto.response.lesson;

import com.sunny.microservices.course.dto.request.lesson.ExamRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonDetailResponse {
    String videoUrl;
    String article;

    List<ExamRequest.QuestionRequest> contents;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionRequest {

        @NotEmpty(message = "QUESTION_REQUIRED")
        private String question;

        @NotEmpty(message = "OPTION_REQUIRED")
        private List<ExamRequest.OptionRequest> options;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionRequest {
        private String optionText;

        private Boolean isCorrect;
    }
}
