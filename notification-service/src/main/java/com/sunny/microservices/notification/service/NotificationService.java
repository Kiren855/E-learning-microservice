package com.sunny.microservices.notification.service;


import com.sunny.microservices.basedomain.event.ApproveCourseEvent;
import com.sunny.microservices.basedomain.event.LessonCreatedEvent;
import com.sunny.microservices.basedomain.event.RejectionCourseEvent;
import com.sunny.microservices.basedomain.event.SubmitCourseEvent;
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
        log.info("Received message: {}", event);

        String to = event.getEmail();
        String subject = "Bài học của bạn đã được upload thành công!";
        String content = "Hello bạn " + event.getUsername() + ",\n\n" +
                "Bài học '" + event.getLessonName() + "' hiện đã được hoàn thành lưu trên hệ thống.\n\n" +
                "Để kiểm tra, bạn có thể vào quản lý khoá học của bạn, nếu có vấn đề hãy liên lạc lại cho chúng tuôi";

        emailService.sendEmail(to, subject, content);
    }

    @KafkaListener(topics = "submit-course")
    public void listen(SubmitCourseEvent event) {
        String adminTo = "cngitcompany@gmail.com";
        log.info("đã nhận được thông báo submit course");

        String subject = "Thông báo: Người dùng mong muốn submit khoá học";
        String content = "Chào admin,\n\n" +
                "Người dùng '" + event.getUsername() + "' đã gửi yêu cầu đăng ký khóa học mới.\n\n" +
                "Tên khóa học: " + event.getCourseName() + "\n" +
                "Vui lòng kiểm tra và duyệt yêu cầu này nếu cần.";

        emailService.sendEmail(adminTo, subject, content);
    }

    @KafkaListener(topics = "approve-course")
    public void listen(ApproveCourseEvent event) {
        String to = event.getEmail();
        log.info("Đã nhận được thông báo chấp thuận course");

        String subject = "Thông báo: Khoá học của bạn đã được chấp thuận";
        String content = "Chào " + event.getUsername() + ", \n\n" +
                "Khoá học '" + event.getCourseName() + "' đã được chấp thuận. \n\n" +
                "Từ bây giờ khoá học của bạn đã có thể sử dụng để thương mại, " +
                "các học viên giờ có thể thấy được khoá học của bạn ở trên trang chủ. \n" +
                "Bạn nên kiểm soát khoá học của bạn cẩn thận, vì chúng tôi luôn theo dõi bạn, " +
                "nếu khoá học của bạn vi phạm 1 trong các chinh sách của chúng tôi, " +
                "thì bạn sẽ không còn có thể sử dụng được hệ thống của chúng tôi nữa!.";

        emailService.sendEmail(to, subject, content);
    }

    @KafkaListener(topics = "reject-course")
    public void listen(RejectionCourseEvent event) {
        String to = event.getEmail();
        log.info("Đã nhận được thông báo từ chối course");

        String subject = "Thông báo: Khoá học của bạn không được chấp thuận";

        String content = "Chào " + event.getUsername() + ", \n\n" +
                "Khoá học '" + event.getCourseName() + "' không được chấp thuận. \n\n" +
                "Lý do : " + event.getReason();

        emailService.sendEmail(to, subject, content);
    }
}
