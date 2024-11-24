package com.sunny.microservices.course.dto.request.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseNotiRequest {
    String welcome;
    String congratulation;
}
