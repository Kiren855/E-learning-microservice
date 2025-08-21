package com.sunny.microservices.order.kafka;

import com.sunny.microservices.basedomain.event.PaymentEvent;
import com.sunny.microservices.order.entity.PaymentStatus;
import com.sunny.microservices.order.repository.PaymentStatusRepository;
import com.sunny.microservices.order.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderedConsumer {
    CartService cartService;
    PaymentStatusRepository paymentStatusRepository;
    @KafkaListener(topics = "payment-success")
    public void listen(PaymentEvent event) {
        if(event.getStatus().equals("COMPLETED")) {
            List<PaymentStatus> paymentStatuses = event.getCourses().stream()
                    .map(course -> PaymentStatus.builder()
                            .userId(event.getUserId())
                            .courseId(course.getCourseId())
                            .build())
                    .toList();

            if (!paymentStatuses.isEmpty()) {
                paymentStatusRepository.saveAll(paymentStatuses);
            }

            cartService.removeCourseOrderedFromCart(event.getUserId(), event.getCourses());
        }
    }
}
