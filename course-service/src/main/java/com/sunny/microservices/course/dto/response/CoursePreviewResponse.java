package com.sunny.microservices.course.dto.response;


import com.sunny.microservices.course.dto.DTO.ReviewDetail;
import com.sunny.microservices.course.dto.DTO.SectionPreview;
import com.sunny.microservices.course.dto.DTO.TopicPreview;
import com.sunny.microservices.course.entity.Section;
import com.sunny.microservices.course.entity.Topic;
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
    List<TopicPreview> topic;
    String instructor;
    List<SectionPreview> sections;
    Double rating;
    List<String> language;
    Double price;
    Double discount;
    List<ReviewDetail> reviews;
    List<String> targetAudiences;
    List<String> requirements;
    Double duration;
}
