package com.sunny.microservices.course.dto.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubmitCourseDto {
    String courseId;
    String courseName;
    String instructor;
}
