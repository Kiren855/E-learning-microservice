package com.sunny.microservices.course.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("pendingcourses")
public class PendingCourse {
    @Id
    String id;
    String courseId;
    String courseName;
    String instructor;
}
