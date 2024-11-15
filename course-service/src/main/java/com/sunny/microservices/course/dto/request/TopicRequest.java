package com.sunny.microservices.course.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicRequest {
    @Size(min = 3, message = "INVALID_MIN_MAX_TOPIC_NAME")
    String name;
    String description;
}
