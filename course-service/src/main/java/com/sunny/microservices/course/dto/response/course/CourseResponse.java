package com.sunny.microservices.course.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  CourseResponse {
    String id;
    String image;
    String title;
    Boolean isDraft;
}
