package com.sunny.microservices.learning.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("enrollments")
public class Enrollment {
    @Id
    String id;
    String userId;
    String courseId;
    String status;
    Progress progress;
    Date enrollmentDate;
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Progress {
        List<String> completedLessons;
        Integer totalLessons;
        Double completionRate;
    }
}