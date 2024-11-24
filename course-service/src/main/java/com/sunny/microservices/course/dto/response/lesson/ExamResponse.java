package com.sunny.microservices.course.dto.response.lesson;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamResponse {
    String title;
    String subTitle;
    List<QuestionResponse> contents;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionResponse {

        @NotEmpty(message = "QUESTION_REQUIRED")
        private String question;

        @NotEmpty(message = "OPTION_REQUIRED")
        private List<OptionResponse> options;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionResponse {
        private String optionText;

        private Boolean isCorrect;
    }
}
