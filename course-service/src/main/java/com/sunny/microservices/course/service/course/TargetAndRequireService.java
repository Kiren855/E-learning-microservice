package com.sunny.microservices.course.service.course;

import com.sunny.microservices.course.dto.request.course.TargetAndRequireRequest;
import com.sunny.microservices.course.dto.response.course.TargetAndRequireResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.CourseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TargetAndRequireService {
    CourseRepository courseRepository;

    public String updateTargetAndRequired(String courseId, TargetAndRequireRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        if(request.getRequirements() != null) {
            course.setRequirements(request.getRequirements());
            courseRepository.save(course);
        }
        if(request.getTargetAudiences() != null) {
            course.setTargetAudiences(request.getTargetAudiences());
            courseRepository.save(course);
        }
        return "Cập nhật thành công";
    }

    public TargetAndRequireResponse getTargetAndRequire(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        TargetAndRequireResponse response = TargetAndRequireResponse.builder().build();

        if(course.getTargetAudiences() == null)
            response.setTargetAudiences(new ArrayList<>());
        else
            response.setTargetAudiences(course.getTargetAudiences());

        if(course.getRequirements() == null)
            response.setRequirements(new ArrayList<>());
        else
            response.setRequirements(course.getRequirements());

        return response;
    }
}
