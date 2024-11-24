package com.sunny.microservices.course.service.course;

import com.sunny.microservices.course.dto.request.course.CourseNotiRequest;
import com.sunny.microservices.course.dto.response.course.CourseNotiResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.CourseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseNotiService {
    CourseRepository courseRepository;
    public String updateCourseNoti(String courseId, CourseNotiRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        if (request.getWelcome() != null && !request.getWelcome().isEmpty()) {
            course.setWelcome(request.getWelcome());
        }
        if (request.getCongratulation() != null && !request.getCongratulation().isEmpty()) {
            course.setCongratulation(request.getCongratulation());
        }
        courseRepository.save(course);

        return "Cập nhật thành công thông báo khoá học";
    }

    public CourseNotiResponse getCourseNoti(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        return CourseNotiResponse.builder()
                .welcome(course.getWelcome())
                .congratulation(course.getCongratulation())
                .build();
    }
}
