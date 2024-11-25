package com.sunny.microservices.payment.controller;

import com.sunny.microservices.payment.dto.response.IpnResponse;
import com.sunny.microservices.payment.service.IpnHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final IpnHandler ipnHandler;

    @GetMapping("/vnpay_ipn")
    IpnResponse processIpn(@RequestParam Map<String, String> params) {
        return ipnHandler.process(params);
    }
}

