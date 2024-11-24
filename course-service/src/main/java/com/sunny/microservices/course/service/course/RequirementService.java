package com.sunny.microservices.course.service.course;

import com.sunny.microservices.course.dto.request.course.CourseRequirementRequest;
import com.sunny.microservices.course.dto.request.course.CourseTargetRequest;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.CourseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequirementService {
    CourseRepository courseRepository;

    public String updateRequirement(String courseId, CourseRequirementRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        course.setRequirements(request.getRequirements());
        courseRepository.save(course);

        return "cập nhật danh sách yêu cầu khoá học thành công";
    }

    public List<String> getRequirement(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        return course.getRequirements();
    }
}
