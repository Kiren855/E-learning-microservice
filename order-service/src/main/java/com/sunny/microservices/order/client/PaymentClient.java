package com.sunny.microservices.order.client;

import com.sunny.microservices.basedomain.payment.dto.InitPaymentRequest;
import com.sunny.microservices.basedomain.payment.dto.InitPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment", url = "${payment-service.url}")
public interface PaymentClient {
    @PostMapping("/internal")
    InitPaymentResponse init(@RequestBody InitPaymentRequest request);

    @GetMapping("/internal")
    String show();
}
