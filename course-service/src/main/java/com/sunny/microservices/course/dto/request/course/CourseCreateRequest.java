package com.sunny.microservices.course.dto.request.course;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseCreateRequest {
    @NotEmpty(message = "TITLE_REQUIRED")
    String title;

    @NotEmpty(message = "TOPIC_REQUIRED")
    String mainTopic;
}
