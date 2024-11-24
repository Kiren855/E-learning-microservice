package com.sunny.microservices.order.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishCourseRequest {
    String courseId;
    String courseName;
    Integer price;
}
