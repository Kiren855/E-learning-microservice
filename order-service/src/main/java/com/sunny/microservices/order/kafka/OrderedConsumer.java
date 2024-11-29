package com.sunny.microservices.order.kafka;

import com.sunny.microservices.basedomain.event.PaymentEvent;
import com.sunny.microservices.order.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderedConsumer {
    CartService cartService;
    @KafkaListener(topics = "payment-success")
    public void listen(PaymentEvent event) {
        if(event.getStatus().equals("COMPLETED")) {
            cartService.removeCourseOrderedFromCart(event.getUserId(), event.getCourses());
        }
    }
}
