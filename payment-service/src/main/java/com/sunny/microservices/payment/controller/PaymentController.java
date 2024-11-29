package com.sunny.microservices.payment.controller;

import com.sunny.microservices.basedomain.event.PaymentEvent;
import com.sunny.microservices.basedomain.payment.dto.InitPaymentRequest;
import com.sunny.microservices.payment.dto.response.ApiResponse;
import com.sunny.microservices.payment.dto.response.IpnResponse;
import com.sunny.microservices.payment.entity.PaymentHistory;
import com.sunny.microservices.payment.exception.AppException;
import com.sunny.microservices.payment.exception.ErrorCode;
import com.sunny.microservices.payment.kafka.PaymentProducer;
import com.sunny.microservices.payment.repository.PaymentHistoryRepository;
import com.sunny.microservices.payment.service.IpnHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final IpnHandler ipnHandler;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentProducer paymentProducer;
    @GetMapping("/vnpay_ipn")
    IpnResponse processIpn(@RequestParam Map<String, String> params) {
        return ipnHandler.process(params);
    }

    @GetMapping("/vnpay-callback")
    ResponseEntity<ApiResponse<String>> payHandle(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        ApiResponse<String> response = ApiResponse.<String>builder().build();
        String orderId = request.getParameter("vnp_TxnRef");
        PaymentHistory paymentHistory = paymentHistoryRepository.findByOrderId(orderId)
                .orElseThrow(()-> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        if(Objects.equals(status, "00")) {
            paymentHistory.setStatus("COMPLETED");

            List<PaymentEvent.Course> courses = paymentHistory.getCourses().stream().map(
                    course -> PaymentEvent.Course.builder()
                            .courseId(course.getCourseId())
                            .instructorName(course.getInstructorName())
                            .courseName(course.getCourseName()).build()
            ).toList();

            PaymentEvent event = PaymentEvent.builder()
                    .userId(paymentHistory.getUserId())
                    .status(paymentHistory.getStatus())
                    .courses(courses).build();

            paymentProducer.sendMessage(event);
            response.setMessage("thanh toán thành công");
        }else {
            paymentHistory.setStatus("FAILED");
            response.setMessage("thanh toán thất bại");
        }
        paymentHistoryRepository.save(paymentHistory);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

