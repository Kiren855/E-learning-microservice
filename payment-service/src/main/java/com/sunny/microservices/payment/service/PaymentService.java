package com.sunny.microservices.payment.service;

import com.sunny.microservices.basedomain.payment.dto.InitPaymentRequest;
import com.sunny.microservices.basedomain.event.InitPaymentResponse;

public interface PaymentService {
    InitPaymentResponse init(InitPaymentRequest request);
}
