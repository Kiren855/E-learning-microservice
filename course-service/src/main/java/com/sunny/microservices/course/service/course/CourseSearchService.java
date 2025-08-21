package com.sunny.microservices.course.service.course;

import com.sunny.microservices.course.dto.response.course.CourseSearchResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.repository.CourseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseSearchService {
    CourseRepository courseRepository;

    public Page<CourseSearchResponse> searchCourseByTitle(String keyword, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        return courseRepository
                .findByTitleContainingIgnoreCaseOrInstructorNameContainingIgnoreCaseOrSubTitleContainingIgnoreCase(
                        keyword, keyword, keyword, pageRequest)
                .map(course -> CourseSearchResponse.builder()
                        .id(course.getId())
                        .courseName(course.getTitle())
                        .subTitle(course.getSubTitle())
                        .instructorName(course.getInstructorName())
                        .image(course.getImage())
                        .rating(course.getRating())
                        .totalReview(course.getReviews() == null ? 0 : course.getReviews().size())
                        .price(course.getPrice())
                        .build());
    }
}
