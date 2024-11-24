package com.sunny.microservices.course.kafka;

import com.sunny.microservices.basedomain.course.dto.event.SubmitCourseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmitCourseProducer {
    private final KafkaTemplate<String, SubmitCourseEvent> kafkaTemplate;

    @Value("submit-course")
    private String topicName;
    public void sendMessage(SubmitCourseEvent event) {
        Message<SubmitCourseEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        kafkaTemplate.send(message);
    }
}
