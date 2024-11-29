package com.sunny.microservices.payment.kafka;

import com.sunny.microservices.basedomain.event.ApproveCourseEvent;
import com.sunny.microservices.basedomain.event.PaymentEvent;
import com.sunny.microservices.payment.entity.PaymentHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducer {
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Value("payment-success")
    private String topicName;
    public void sendMessage(PaymentEvent event) {
        Message<PaymentEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        kafkaTemplate.send(message);
    }
}