package com.sunny.microservices.course.dto.response.course;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseHomePageResponse {
    String courseId;
    String courseName;
    String instructorName;
    String image;
    Double rating;
    Integer price;
}
