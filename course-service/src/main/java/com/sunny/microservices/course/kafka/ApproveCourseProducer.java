package com.sunny.microservices.course.kafka;


import com.sunny.microservices.basedomain.event.ApproveCourseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApproveCourseProducer {
    private final KafkaTemplate<String, ApproveCourseEvent> kafkaTemplate;

    @Value("approve-course")
    private String topicName;
    public void sendMessage(ApproveCourseEvent event) {
        Message<ApproveCourseEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        kafkaTemplate.send(message);
    }
}
