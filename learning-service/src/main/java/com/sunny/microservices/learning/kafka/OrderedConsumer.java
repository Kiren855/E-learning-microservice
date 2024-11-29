package com.sunny.microservices.learning.kafka;

import com.sunny.microservices.basedomain.event.PaymentEvent;
import com.sunny.microservices.learning.entity.Enrollment;
import com.sunny.microservices.learning.repository.EnrollmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderedConsumer {
    EnrollmentRepository enrollmentRepository;

    @KafkaListener(topics = "payment-success")
    public void listen(PaymentEvent event) {
        List<Enrollment> enrollments = event.getCourses().stream()
                .map(course -> createEnrollment(event, course))
                .toList();

        enrollmentRepository.saveAll(enrollments);
    }

    private Enrollment createEnrollment(PaymentEvent event, PaymentEvent.Course course) {
        Enrollment enrollment = Enrollment.builder()
                .userId(event.getUserId())
                .courseId(course.getCourseId())
                .instructorName(course.getInstructorName())
                .enrollmentDate(Date.from(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant()))
                .status("ĐANG HỌC")
                .build();

        Enrollment.Progress progress = Enrollment.Progress.builder()
                .completedLessons(new HashMap<>())
                .totalLessons(0)
                .completionRate(0.0)
                .build();

        enrollment.setProgress(progress);
        return enrollment;
    }
}
