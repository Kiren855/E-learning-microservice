package com.sunny.microservices.basedomain.event;

import com.sunny.microservices.basedomain.payment.dto.InitPaymentRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentEvent {
    String status;
    String userId;

    private List<Course> courses;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Course {
        String courseId;
        String instructorName;
        String courseName;
        String image;
    }
}
