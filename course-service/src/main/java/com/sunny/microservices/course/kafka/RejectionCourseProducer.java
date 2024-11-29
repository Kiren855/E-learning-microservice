package com.sunny.microservices.course.kafka;

import com.sunny.microservices.basedomain.event.RejectionCourseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RejectionCourseProducer {
    private final KafkaTemplate<String, RejectionCourseEvent> kafkaTemplate;

    @Value("reject-course")
    private String topicName;
    public void sendMessage(RejectionCourseEvent event) {
        Message<RejectionCourseEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        kafkaTemplate.send(message);
    }
}
