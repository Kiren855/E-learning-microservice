package com.sunny.microservices.course.dto.response.course;


import com.sunny.microservices.basedomain.course.dto.DTO.ReviewDetail;
import com.sunny.microservices.course.dto.DTO.SectionPreview;
import com.sunny.microservices.course.dto.DTO.TopicPreview;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoursePreviewResponse  implements Serializable {
    String id;
    String image;
    String title;
    String subTitle;
    String description;
    String mainTopic;
    String subTopic;
    String instructorId;
    String instructorName;
    Double duration;
    Double rating;
    String language;
    Integer price;
    Double discount;
    String welcome;
    String congratulation;
    List<ReviewDetail> reviews;
    List<String> targetAudiences;
    List<String> requirements;
    List<SectionPreview> sections;
}
