package com.sunny.microservices.order.service;

import com.sunny.microservices.basedomain.payment.dto.InitPaymentRequest;
import com.sunny.microservices.basedomain.payment.dto.InitPaymentResponse;
import com.sunny.microservices.order.client.PaymentClient;
import com.sunny.microservices.order.dto.request.OrderRequest;
import com.sunny.microservices.order.dto.response.OrderDto;
import com.sunny.microservices.order.dto.response.OrderResponse;
import com.sunny.microservices.order.entity.Cart;
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

        List<Cart> carts = cartService.getCoursesInCart();

        int totalPrice = carts.stream()
                .mapToInt(Cart::getPrice)
                .sum();

        String orderId = UUID.randomUUID().toString().substring(0, 10);
        String requestId = UUID.randomUUID().toString().substring(0, 12);

        InitPaymentRequest initPaymentRequest = InitPaymentRequest.builder()
                .userId(userId)
                .amount(totalPrice)
                .txnRef(orderId)
                .ipAddress(request.getIpAddress())
                .requestId(requestId)
                .build();

        InitPaymentResponse initPaymentResponse = paymentClient.init(initPaymentRequest);

        OrderDto orderDto = OrderDto.builder()
                .userId(userId)
                .price(totalPrice)
                .build();

        return OrderResponse.builder()
                .order(orderDto)
                .payment(initPaymentResponse)
                .build();

    }
}
