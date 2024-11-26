package com.sunny.microservices.course.dto.response.course;

import com.sunny.microservices.course.dto.DTO.SectionDetail;
import com.sunny.microservices.course.dto.DTO.SectionPreview;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseDetailResponse {
    String id;
    String image;
    String title;
    String subTitle;
    String description;
    String mainTopic;
    String subTopic;
    String instructorId;
    String instructorName;
    String language;
    Integer price;
    String welcome;
    String congratulation;
    List<String> targetAudiences;
    List<String> requirements;
    List<SectionDetail> sections;
}
