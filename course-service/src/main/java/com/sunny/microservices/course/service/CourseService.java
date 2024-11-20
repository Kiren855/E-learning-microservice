package com.sunny.microservices.course.service;

import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.dto.DTO.ReviewDetail;
import com.sunny.microservices.course.dto.DTO.SectionDetail;
import com.sunny.microservices.course.dto.DTO.SectionPreview;
import com.sunny.microservices.course.dto.DTO.TopicPreview;
import com.sunny.microservices.course.dto.request.CourseRequest;
import com.sunny.microservices.course.dto.response.CourseDetailResponse;
import com.sunny.microservices.course.dto.response.CoursePreviewResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.CourseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

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

    @Value("${azure.blob.doc-container}")
    @NonFinal
    String docContainer;
    //@Cacheable(value = "coursePreviewCache", key = "#courseId")
    public CoursePreviewResponse getCoursePreview(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        List<TopicPreview> topics = topicService.findTopicsByIds(course.getTopic());
        List<SectionPreview> sections = sectionService.findSectionsByIds(course.getSections());
        List<ReviewDetail> reviews = reviewService.findReviewsById(course.getReviews());
        Double totalDuration = sections.stream().mapToDouble(SectionPreview::getDuration).sum();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String name = jwt.getClaim("name");

        return CoursePreviewResponse.builder()
                .id(course.getId())
                .image(course.getImage())
                .title(course.getTitle())
                .subTitle(course.getSubTitle())
                .description(course.getDescription())
                .topic(topics)
                .instructor(name)
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
    public String createCourse(CourseRequest request) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();

            String imgPath = azureFileStorageClient.uploadFile(docContainer, Objects.requireNonNull(request.getImage().getOriginalFilename()), request.getImage().getInputStream(), request.getImage().getSize());
            Course course = Course.builder()
                    .image(imgPath)
                    .title(request.getTitle())
                    .subTitle(request.getSubTitle())
                    .description(request.getDescription())
                    .topic(request.getTopic())
                    .language(request.getLanguage())
                    .price(request.getPrice())
                    .targetAudiences(request.getTargetAudiences())
                    .requirements(request.getRequirements())
                    .instructor(userId)
                    .isDraft(Boolean.TRUE)
                    .discount(0.0)
                    .rating(0.0)
                    .reviews(List.of())
                    .sections(List.of()).build();

            courseRepository.save(course);

            return "tạo khoá học thành công";
        }catch (IOException e) {
            throw new AppException(ErrorCode.FILE_INVALID);
        }
    }

//    @Caching(evict = {
//            @CacheEvict(value = "coursePreviewCache", key = "#id"),
//            @CacheEvict(value = "courseDetailsCache", key = "#id")
//    })
    public void updateCourse(String courseId,CourseRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        course.setTitle(request.getTitle());
        course.setSubTitle(request.getSubTitle());
        course.setDescription(request.getDescription());
        course.setTopic(request.getTopic());
        course.setLanguage(request.getLanguage());
        course.setPrice(request.getPrice());
        course.setTargetAudiences(request.getTargetAudiences());

        courseRepository.save(course);
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

        return CourseDetailResponse.builder()
                .id(course.getId())
                .sections(sections).build();
    }

}
