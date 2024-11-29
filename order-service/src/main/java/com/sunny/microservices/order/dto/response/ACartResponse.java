package com.sunny.microservices.order.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ACartResponse {
    String id;
    String userId;
    String courseId;
    String image;
    String instructorName;
    String courseName;
    Integer price;
}
