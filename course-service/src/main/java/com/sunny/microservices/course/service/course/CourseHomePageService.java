package com.sunny.microservices.course.service.course;

import com.sunny.microservices.course.dto.response.course.CourseHomePageResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.repository.CourseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseHomePageService {
    CourseRepository courseRepository;

    public List<CourseHomePageResponse> getCourses() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream()
                .filter(course -> !course.getIsDraft())
                .limit(5)
                .map(course -> CourseHomePageResponse.builder()
                        .courseId(course.getId())
                        .courseName(course.getTitle())
                        .instructorName(course.getInstructorName())
                        .rating(course.getRating())
                        .image(course.getImage())
                        .price(course.getPrice()).build())
                .collect(Collectors.toList());
    }

    public List<CourseHomePageResponse> getCourseByMainTopic(String topicId) {
            List<Course> courses = courseRepository.findByMainTopic(topicId);

        return courses.stream()
                .filter(course -> !course.getIsDraft())
                .limit(5)
                .map(course -> CourseHomePageResponse.builder()
                        .courseId(course.getId())
                        .courseName(course.getTitle())
                        .instructorName(course.getInstructorName())
                        .rating(course.getRating())
                        .image(course.getImage())
                        .price(course.getPrice()).build())
                .collect(Collectors.toList());
    }
}
