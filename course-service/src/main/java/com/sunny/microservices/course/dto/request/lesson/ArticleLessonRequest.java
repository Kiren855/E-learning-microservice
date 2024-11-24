package com.sunny.microservices.course.dto.request.lesson;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleLessonRequest {
    String name;
    Integer partNumber;
    String content;
}
