package com.sunny.microservices.order.controller;

import com.sunny.microservices.basedomain.payment.dto.ResponseDto;
import com.sunny.microservices.basedomain.payment.util.RequestUtil;
import com.sunny.microservices.order.client.PaymentClient;
import com.sunny.microservices.order.dto.request.OrderRequest;
import com.sunny.microservices.order.service.OrderService;
import com.sunny.microservices.order.service.ResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final ResponseFactory responseFactory;
    private final OrderService orderService;
    private final PaymentClient paymentClient;
    @GetMapping
    public String show() {
        return paymentClient.show();
    }
    @PostMapping("/order-payment")
    public ResponseDto orderCourse(@Valid @RequestBody OrderRequest request,
                            HttpServletRequest httpServletRequest) {
        var ipAddress = RequestUtil.getIpAddress(httpServletRequest);
        request.setIpAddress(ipAddress);
        var response = orderService.order(request);

        return responseFactory.success(response);
    }
}
