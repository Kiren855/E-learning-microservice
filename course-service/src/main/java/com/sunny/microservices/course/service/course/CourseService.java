package com.sunny.microservices.course.service.course;

import com.sunny.microservices.basedomain.course.dto.DTO.ReviewDetail;
import com.sunny.microservices.basedomain.course.dto.DTO.SectionDetail;
import com.sunny.microservices.basedomain.course.dto.DTO.TopicDetail;
import com.sunny.microservices.basedomain.course.dto.response.CourseDetailResponse;
import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.client.UserClient;
import com.sunny.microservices.course.dto.DTO.SectionPreview;
import com.sunny.microservices.course.dto.DTO.TopicPreview;
import com.sunny.microservices.course.dto.request.course.CourseRequest;
import com.sunny.microservices.course.dto.response.course.CoursePreviewResponse;
import com.sunny.microservices.course.dto.response.course.CourseResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.CourseRepository;
import com.sunny.microservices.course.service.ReviewService;
import com.sunny.microservices.course.service.SectionService;
import com.sunny.microservices.course.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseService {
    CourseRepository courseRepository;
    TopicService topicService;
    SectionService sectionService;
    AzureFileStorageClient azureFileStorageClient;
    ReviewService reviewService;
    UserClient userClient;

    @Value("${azure.blob.doc-container}")
    @NonFinal
    String docContainer;
    public CoursePreviewResponse getCoursePreview(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        List<SectionPreview> sections = sectionService.findSectionsByIds(course.getSections());
        List<ReviewDetail> reviews = reviewService.findReviewsById(course.getReviews());
        Double totalDuration = sections.stream().mapToDouble(SectionPreview::getDuration).sum();
        String username = userClient.getProfile(course.getInstructor()).getUsername();
        String mainTopic = topicService.getMainTopicById(course.getMainTopic());
        String subTopic = topicService.getSubTopicById(course.getSubTopic());

        return CoursePreviewResponse.builder()
                .id(course.getId())
                .image(course.getImage())
                .title(course.getTitle())
                .subTitle(course.getSubTitle())
                .description(course.getDescription())
                .mainTopic(mainTopic)
                .subTopic(subTopic)
                .instructor(username)
                .sections(sections)
                .rating(course.getRating())
                .language(course.getLanguage())
                .price(course.getPrice())
                .discount(course.getDiscount())
                .reviews(reviews)
                .targetAudiences(course.getTargetAudiences())
                .requirements(course.getRequirements())
                .duration(totalDuration).build();
    }

    public void deleteCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if(course.getIsDraft() == Boolean.TRUE) {
            throw new AppException(ErrorCode.COURSE_CANNOT_DELETE);
        }
        if(course.getSections().isEmpty()) {
            courseRepository.delete(course);
        }else {
            throw new AppException(ErrorCode.SECTION_NOT_EMPTY);
        }
        courseRepository.delete(course);
    }

    public CourseDetailResponse getCourseDetail(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        List<SectionDetail> sections = sectionService.findSectionsDetailByIds(course.getSections());
        List<ReviewDetail> reviews = reviewService.findReviewsById(course.getReviews());
        Double totalDuration = sections.stream().mapToDouble(SectionDetail::getDuration).sum();
        String username = userClient.getProfile(course.getInstructor()).getUsername();
        String mainTopic = topicService.getMainTopicById(course.getMainTopic());
        String subTopic = topicService.getSubTopicById(course.getSubTopic());

        return CourseDetailResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .subTitle(course.getSubTitle())
                .description(course.getDescription())
                .mainTopic(mainTopic)
                .subTopic(subTopic)
                .sections(sections)
                .instructor(username)
                .language(course.getLanguage())
                .rating(course.getRating())
                .reviews(reviews)
                .targetAudiences(course.getTargetAudiences())
                .requirements(course.getRequirements())
                .duration(totalDuration).build();
    }

    public List<CourseResponse> getCourses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<Course> courses = courseRepository.findByInstructor(userId);

        return courses.stream().map(course ->
        {
            return CourseResponse.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .image(course.getImage())
                    .isDraft(course.getIsDraft())
                    .build();
        }).collect(Collectors.toList());
    }

}
