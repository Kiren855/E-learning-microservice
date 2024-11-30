package com.sunny.microservices.course.service.course;

import com.sunny.microservices.course.client.UserClient;
import com.sunny.microservices.course.dto.request.course.CourseCreateRequest;
import com.sunny.microservices.course.dto.response.IdResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.repository.CourseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseCreateService {
    CourseRepository courseRepository;
    CourseProcessingService courseProcessingService;
    public IdResponse createCourse(CourseCreateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Course course = Course.builder().build();
        course.setTitle(request.getTitle());
        course.setMainTopic(request.getMainTopic());
        course.setInstructorId(userId);
        course.setPrice(0);
        course.setSections(new ArrayList<>());
        course.setIsDraft(Boolean.TRUE);
        course.setRating(0.0);
        courseRepository.save(course);

        courseProcessingService.processUpdateInstructorInCourse(userId, course);

        return IdResponse.builder().Id(course.getId()).build();
    }
}
