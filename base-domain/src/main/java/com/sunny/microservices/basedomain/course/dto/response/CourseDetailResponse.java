package com.sunny.microservices.basedomain.course.dto.response;


import com.sunny.microservices.basedomain.course.dto.DTO.ReviewDetail;
import com.sunny.microservices.basedomain.course.dto.DTO.SectionDetail;
import com.sunny.microservices.basedomain.course.dto.DTO.TopicDetail;
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
    String title;
    String subTitle;
    String description;
    String mainTopic;
    String subTopic;
    List<SectionDetail> sections;
    String instructorId;
    String instructorName;
    Double rating;
    String language;
    List<String> targetAudiences;
    List<String> requirements;
    Double duration;
    List<ReviewDetail> reviews;
}
