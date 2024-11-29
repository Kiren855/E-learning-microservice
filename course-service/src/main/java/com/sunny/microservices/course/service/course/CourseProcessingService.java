package com.sunny.microservices.course.service.course;

import com.sunny.microservices.basedomain.event.ApproveCourseEvent;
import com.sunny.microservices.basedomain.event.RejectionCourseEvent;
import com.sunny.microservices.basedomain.event.SubmitCourseEvent;
import com.sunny.microservices.course.client.UserClient;
import com.sunny.microservices.course.dto.response.ProfileResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.entity.PendingCourse;
import com.sunny.microservices.course.kafka.ApproveCourseProducer;
import com.sunny.microservices.course.kafka.RejectionCourseProducer;
import com.sunny.microservices.course.kafka.SubmitCourseProducer;
import com.sunny.microservices.course.repository.CourseRepository;
import com.sunny.microservices.course.repository.PendingCourseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseProcessingService {
    UserClient userClient;
    PendingCourseRepository pendingCourseRepository;
    SubmitCourseProducer submitCourseProducer;
    ApproveCourseProducer approveCourseProducer;
    RejectionCourseProducer rejectionCourseProducer;
    CourseRepository courseRepository;
    @Async("threadPoolTaskExecutor")
    public void processSubmitCourse(String courseId, String userId, String courseName) {
        ProfileResponse user = userClient.getProfile(userId);

        PendingCourse pendingCourse = PendingCourse.builder()
                .courseId(courseId)
                .courseName(courseName)
                .instructor(user.getUsername()).build();

       pendingCourseRepository.save(pendingCourse);

        log.info("bắt đầu submit course");
        SubmitCourseEvent event = new SubmitCourseEvent(courseName, user.getUsername());

        submitCourseProducer.sendMessage(event);
    }

    @Async("threadPoolTaskExecutor")
    public void processApproveCourse(String userId, String courseName) {
        ProfileResponse user = userClient.getProfile(userId);

        log.info("bắt đầu chấp thuận khoá học");

        ApproveCourseEvent event = ApproveCourseEvent.builder()
                .courseName(courseName)
                .username(user.getUsername())
                .email(user.getEmail()).build();

        approveCourseProducer.sendMessage(event);
    }

    @Async("threadPoolTaskExecutor")
    public void processRejectCourse(String userId, String courseName, String reason) {
        ProfileResponse user = userClient.getProfile(userId);

        log.info("bắt đầu từ chối khoá học");

        RejectionCourseEvent event = RejectionCourseEvent.builder()
                .courseName(courseName)
                .email(user.getEmail())
                .username(user.getUsername())
                .reason(reason).build();

        rejectionCourseProducer.sendMessage(event);
    }

    @Async("threadPoolTaskExecutor")
    public void processUpdateInstructorInCourse(String userId, Course course) {
        ProfileResponse user = userClient.getProfile(userId);

        course.setInstructorName(user.getUsername());
        courseRepository.save(course);
    }
}

