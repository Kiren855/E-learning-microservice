package com.sunny.microservices.order.service;

import com.sunny.microservices.basedomain.payment.dto.InitPaymentRequest;
import com.sunny.microservices.basedomain.event.InitPaymentResponse;
import com.sunny.microservices.order.client.PaymentClient;
import com.sunny.microservices.order.dto.request.OrderRequest;
import com.sunny.microservices.order.dto.response.ACartResponse;
import com.sunny.microservices.order.dto.response.CartResponse;
import com.sunny.microservices.order.dto.response.OrderDto;
import com.sunny.microservices.order.dto.response.OrderResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    PaymentClient paymentClient;
    CartService cartService;

    @SneakyThrows
    public OrderResponse order(OrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        CartResponse cartResponse = cartService.getCoursesInCart();
        List<ACartResponse> carts = cartResponse.getCart();

        List<InitPaymentRequest.Course> courses = carts.stream().map(cart -> InitPaymentRequest.Course.builder()
                .courseId(cart.getCourseId())
                .instructorName(cart.getInstructorName())
                .courseName(cart.getCourseName())
                .price(cart.getPrice()).build()).toList();

        Integer totalPrice = carts.stream().mapToInt(ACartResponse::getPrice).sum();

        String orderId = UUID.randomUUID().toString();
        String requestId = UUID.randomUUID().toString();

        InitPaymentRequest initPaymentRequest = InitPaymentRequest.builder()
                .amount(totalPrice)
                .userId(userId)
                .txnRef(orderId)
                .courses(courses)
                .ipAddress(request.getIpAddress())
                .requestId(requestId)
                .build();

        InitPaymentResponse initPaymentResponse = paymentClient.init(initPaymentRequest);

        OrderDto orderDto = OrderDto.builder()
                .userId(userId)
                .build();

        return OrderResponse.builder()
                .order(orderDto)
                .payment(initPaymentResponse)
                .build();

    }
}
