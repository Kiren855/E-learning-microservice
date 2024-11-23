package com.sunny.microservices.course.dto.request.course;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseOverviewRequest {
    String title;
    String subTitle;
    String description;
    String language;
    String mainTopic;
    String subTopic;
    MultipartFile image;
}
