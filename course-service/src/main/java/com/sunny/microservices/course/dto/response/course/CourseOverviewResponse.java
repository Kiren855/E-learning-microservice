package com.sunny.microservices.course.dto.response.course;

import com.sunny.microservices.course.entity.SubTopic;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseOverviewResponse {
    String title;
    String subTitle;
    String description;
    String instructorId;
    String instructorName;
    String mainTopic;
    String subTopic;
    String image;
}
