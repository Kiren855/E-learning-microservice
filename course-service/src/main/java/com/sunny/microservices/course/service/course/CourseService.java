package com.sunny.microservices.course.service.course;

import com.sunny.microservices.basedomain.course.dto.DTO.ReviewDetail;
import com.sunny.microservices.basedomain.course.dto.DTO.SectionLearning;
import com.sunny.microservices.basedomain.course.dto.response.CourseLearningResponse;
import com.sunny.microservices.course.dto.DTO.SectionPreview;
import com.sunny.microservices.course.dto.DTO.SubmitCourseDto;
import com.sunny.microservices.course.dto.response.course.CoursePreviewResponse;
import com.sunny.microservices.course.dto.response.course.CourseResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.entity.PendingCourse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseService {
    CourseRepository courseRepository;
    TopicService topicService;
    SectionService sectionService;
    ReviewService reviewService;
    CourseProcessingService courseProcessingService;
    PendingCourseRepository pendingCourseRepository;

    public CoursePreviewResponse getCoursePreview(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        CoursePreviewResponse coursePreviewResponse = CoursePreviewResponse.builder()
                .id(course.getId())
                .image(course.getImage())
                .title(course.getTitle())
                .subTitle(course.getSubTitle())
                .description(course.getDescription())
                .instructorId(course.getInstructorId())
                .instructorName(course.getInstructorName())
                .rating(course.getRating())
                .language(course.getLanguage())
                .price(course.getPrice())
                .targetAudiences(course.getTargetAudiences())
                .requirements(course.getRequirements())
                .updatedAt(course.getUpdatedAt())
                .build();

        if(course.getMainTopic() == null) {
            coursePreviewResponse.setMainTopic("");
        }else {
            String mainTopic = topicService.getMainTopicById(course.getMainTopic());
            coursePreviewResponse.setMainTopic(mainTopic);
        }

        if(course.getSubTopic() == null)
            coursePreviewResponse.setSubTopic("");
        else {
            String subTopic = topicService.getSubTopicById(course.getSubTopic());
            coursePreviewResponse.setSubTopic(subTopic);
        }

        if(course.getSections() == null || course.getSections().isEmpty())
            coursePreviewResponse.setSections(new ArrayList<>());
        else {
            List<SectionPreview> sections = sectionService.findSectionsByIds(course.getSections());
            Double totalDuration = sections.stream().mapToDouble(SectionPreview::getDuration).sum();
            coursePreviewResponse.setDuration(totalDuration);
            coursePreviewResponse.setSections(sections);
        }

        if(course.getReviews() == null || course.getReviews().isEmpty())
            coursePreviewResponse.setReviews(new ArrayList<>());
        else {
            List<ReviewDetail> reviews = reviewService.findReviewsById(course.getReviews());
            coursePreviewResponse.setReviews(reviews);
        }
         return coursePreviewResponse;
    }

    public void deleteCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        log.info("section: {}", course.getSections());
        if(course.getIsDraft() == Boolean.FALSE) {
            throw new AppException(ErrorCode.COURSE_CANNOT_DELETE);
        }
        if(course.getSections() == null || course.getSections().isEmpty()) {
            courseRepository.delete(course);
        }else {
            throw new AppException(ErrorCode.SECTION_NOT_EMPTY);
        }
    }

    public List<CourseResponse> getCourses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<Course> courses = courseRepository.findByInstructorId(userId);

        return courses.stream().map(course ->
                CourseResponse.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .image(course.getImage())
                        .isDraft(course.getIsDraft())
                        .build()).collect(Collectors.toList());
    }

    public String submitCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        var pendingCourse = pendingCourseRepository.findByCourseId(courseId);
        if(pendingCourse.isPresent()) {
           throw new AppException(ErrorCode.COURSE_IS_PENDING);
        }
        courseProcessingService.processSubmitCourse(courseId, userId, course.getTitle());
        return "Thành công, chờ admin xét duyệt";
    }
    public String approveCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        course.setIsDraft(Boolean.FALSE);
        String userId = course.getInstructorId();
        String courseName = course.getTitle();
        courseRepository.save(course);

        PendingCourse pendingCourse = pendingCourseRepository.findByCourseId(courseId)
                .orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        pendingCourseRepository.delete(pendingCourse);

        courseProcessingService.processApproveCourse(userId, courseName);
        return "Chấp thuận thành công khoá học";
    }

    public String rejectCourse(String courseId, String reason) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        String userId = course.getInstructorId();
        String courseName = course.getTitle();

        PendingCourse pendingCourse = pendingCourseRepository.findByCourseId(courseId)
                .orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        pendingCourseRepository.delete(pendingCourse);

        courseProcessingService.processRejectCourse(userId, courseName, reason);
        return "Từ chối thành công khoá học";
    }

    public List<SubmitCourseDto> getListSubmitCourse() {
        List<PendingCourse> pendingCourses = pendingCourseRepository.findAll();

        return pendingCourses.stream().map(
                course -> SubmitCourseDto.builder()
                        .courseId(course.getCourseId())
                        .courseName(course.getCourseName())
                        .instructor(course.getInstructor()).build()
                )
                .collect(Collectors.toList());
    }

}
