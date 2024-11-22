package com.sunny.microservices.notification.service;

import com.sunny.microservices.notification.exception.AppException;
import com.sunny.microservices.notification.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {

    JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String content)  {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom("admin@edumanabo.online");

            mailSender.send(message);
            log.info("success send mail");
        }catch (MailException e) {
            throw new AppException(ErrorCode.MAIL_CANNOT_SEND);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
