package com.sunny.microservices.course.dto.request.lesson;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamRequest {
    @NotEmpty(message = "NAME_REQUIRED")
    String name;

    @NotEmpty(message = "PART_NUMBER_REQUIRED")
    Integer partNumber;

    @NotEmpty(message = "TITLE_REQUIRED")
    String title;

    @NotEmpty(message = "SUB_TITLE_REQUEST")
    String subTitle;

    @NotEmpty(message = "CONTENT_REQUIRED")
    List<QuestionRequest> contents;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionRequest {

        @NotEmpty(message = "QUESTION_REQUIRED")
        private String question;

        @NotEmpty(message = "OPTION_REQUIRED")
        private List<OptionRequest> options;
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

