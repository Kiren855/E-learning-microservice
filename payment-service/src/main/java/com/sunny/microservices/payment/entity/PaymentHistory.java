package com.sunny.microservices.payment.entity;

import com.sunny.microservices.basedomain.payment.dto.InitPaymentRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("payment-history")
public class PaymentHistory {
    @Id
    String id;
    @Indexed
    String userId;
    String orderId;
    Integer totalPrice;
    List<InitPaymentRequest.Course> courses;
    String status;
}
