package com.sunny.microservices.course.service.course;

import com.sunny.microservices.basedomain.course.dto.DTO.ReviewDetail;
import com.sunny.microservices.basedomain.course.dto.DTO.SectionLearning;
import com.sunny.microservices.basedomain.course.dto.response.CourseLearningResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.CourseRepository;
import com.sunny.microservices.course.repository.PendingCourseRepository;
import com.sunny.microservices.course.service.ReviewService;
import com.sunny.microservices.course.service.SectionService;
import com.sunny.microservices.course.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseClientService {
    CourseRepository courseRepository;
    TopicService topicService;
    SectionService sectionService;
    ReviewService reviewService;

    @Cacheable(value = "course-client", key = "#courseId")
    public CourseLearningResponse getCourseDetail(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        List<SectionLearning> sections = sectionService.findSectionLearningByIds(course.getSections());
        List<ReviewDetail> reviews = reviewService.findReviewsById(course.getReviews());
        Double totalDuration = sections.stream().mapToDouble(SectionLearning::getDuration).sum();
        String mainTopic = topicService.getMainTopicById(course.getMainTopic());
        String subTopic = topicService.getSubTopicById(course.getSubTopic());

        return CourseLearningResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .subTitle(course.getSubTitle())
                .description(course.getDescription())
                .mainTopic(mainTopic)
                .subTopic(subTopic)
                .sections(sections)
                .instructorId(course.getInstructorId())
                .instructorName(course.getInstructorName())
                .language(course.getLanguage())
                .rating(course.getRating())
                .reviews(reviews)
                .targetAudiences(course.getTargetAudiences())
                .requirements(course.getRequirements())
                .duration(totalDuration).build();
    }
}
