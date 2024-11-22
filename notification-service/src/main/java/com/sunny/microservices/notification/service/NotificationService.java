package com.sunny.microservices.notification.service;


import com.sunny.microservices.basedomain.course.dto.event.LessonCreatedEvent;
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
public class NotificationService {

    EmailService emailService;
    @KafkaListener(topics = "${spring.kafka.topic.lesson-created}")
    public void listen(LessonCreatedEvent event) {
        System.out.println("Received message: " + event);
        log.info("Received message: {}", event);
        log.info("email: {}", event.getEmail());

        String to = "kiren10122003@gmail.com";
        String subject = "Bài học của bạn đã được upload thành công!";
        String content = "Hello bạn " + event.getUsername() + ",\n\n" +
                "Bài học '" + event.getLessonName() + "' hiện đã được hoàn thành lưu trên hệ thống.\n\n" +
                "Để kiểm tra, bạn có thể vào quản lý khoá học của bạn, nếu có vấn đề hãy liên lạc lại cho chúng tuôi";

        emailService.sendEmail(to, subject, content);
    }
}
