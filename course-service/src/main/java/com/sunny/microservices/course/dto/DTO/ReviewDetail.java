package com.sunny.microservices.course.dto.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDetail {
    String username;
    Integer rating;
    String content;
}
