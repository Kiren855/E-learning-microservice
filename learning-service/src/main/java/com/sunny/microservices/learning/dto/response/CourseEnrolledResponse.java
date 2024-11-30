package com.sunny.microservices.learning.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseEnrolledResponse {
    String courseId;
    String instructorName;
    String courseName;
    String image;
    Double completionRate;
}
