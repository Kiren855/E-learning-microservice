package com.sunny.microservices.course.dto.DTO;

import com.sunny.microservices.course.dto.response.lesson.ArticleResponse;
import com.sunny.microservices.course.dto.response.lesson.ExamResponse;
import com.sunny.microservices.course.dto.response.lesson.VideoResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonDetail {
    String id;
    String name;
    String type;
    String type_id;
    Integer partNumber;
    VideoResponse video;
    ArticleResponse article;
    ExamResponse exam;
}
