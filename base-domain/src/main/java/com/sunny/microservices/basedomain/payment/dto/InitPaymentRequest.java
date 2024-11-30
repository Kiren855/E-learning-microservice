package com.sunny.microservices.basedomain.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class InitPaymentRequest {
    private String requestId;

    private String ipAddress;

    private String userId;

    private String txnRef;

    private Integer amount;

    private List<Course> courses;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Course {
        String courseId;
        String courseName;
        String instructorName;
        String image;
        Integer price;
    }
}
