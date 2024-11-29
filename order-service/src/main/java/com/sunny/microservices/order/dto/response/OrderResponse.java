package com.sunny.microservices.order.dto.response;

import com.sunny.microservices.basedomain.event.InitPaymentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    OrderDto order;
    InitPaymentResponse payment;
}
