package com.sunny.microservices.payment.controller;

import com.sunny.microservices.basedomain.payment.dto.InitPaymentRequest;
import com.sunny.microservices.basedomain.payment.dto.InitPaymentResponse;
import com.sunny.microservices.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@Slf4j
@RequiredArgsConstructor
public class PaymentClientController {

    private final PaymentService paymentService;

    @GetMapping()
    public String show() {
            return "show payment service";
    }
    @PostMapping()
    public InitPaymentResponse init(@RequestBody InitPaymentRequest request) {
        return paymentService.init(request);
    }
}
