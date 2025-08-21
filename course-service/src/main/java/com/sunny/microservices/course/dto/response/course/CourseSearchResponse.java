package com.sunny.microservices.course.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseSearchResponse {
    String id;
    String courseName;
    String subTitle;
    String instructorName;
    String image;
    Double rating;
    Integer totalReview;
    Integer price;
}
