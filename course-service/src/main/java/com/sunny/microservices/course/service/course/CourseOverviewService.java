package com.sunny.microservices.course.service.course;

import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.dto.DTO.SectionDetail;
import com.sunny.microservices.course.dto.request.course.CourseOverviewRequest;
import com.sunny.microservices.course.dto.response.course.CourseDetailResponse;
import com.sunny.microservices.course.dto.response.course.CourseOverviewResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.CourseRepository;
import com.sunny.microservices.course.service.SectionService;
import com.sunny.microservices.course.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseOverviewService {
    AzureFileStorageClient azureFileStorageClient;
    CourseRepository courseRepository;
    TopicService topicService;
    SectionService sectionService;
    @Value("${azure.blob.doc-container}")
    String docContainer;

    public CourseOverviewResponse updateOverviewCourse(String courseId, CourseOverviewRequest request) {
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            if (request.getTitle() != null && !request.getTitle().isEmpty()) {
                course.setTitle(request.getTitle());
            }

            if (request.getSubTitle() != null && !request.getSubTitle().isEmpty()) {
                course.setSubTitle(request.getSubTitle());
            }

            if (request.getDescription() != null && !request.getDescription().isEmpty()) {
                course.setDescription(request.getDescription());
            }

            if (request.getMainTopic() != null && !request.getMainTopic().isEmpty()) {
                course.setMainTopic(request.getMainTopic());
            }

            if (request.getSubTopic() != null && !request.getSubTopic().isEmpty()) {
                course.setSubTopic(request.getSubTopic());
            }

            if (request.getLanguage() != null && !request.getLanguage().isEmpty()) {
                course.setLanguage(request.getLanguage());
            }

            if (request.getImage() != null && request.getImage().getSize() > 0) {
                String imgPath = azureFileStorageClient.uploadFile(
                        docContainer,
                        Objects.requireNonNull(request.getImage().getOriginalFilename()),
                        request.getImage().getInputStream(),
                        request.getImage().getSize()
                );
                course.setImage(imgPath);
            }

            courseRepository.save(course);
            return CourseOverviewResponse.builder()
                    .title(course.getTitle())
                    .subTitle(course.getSubTitle())
                    .description(course.getDescription())
                    .instructorId(course.getInstructorId())
                    .instructorName(course.getInstructorName())
                    .image(course.getImage())
                    .mainTopic(course.getMainTopic())
                    .subTopic(course.getSubTopic()).build();

        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_INVALID);
        }
    }

    public CourseOverviewResponse getCourseOverview(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        return CourseOverviewResponse.builder()
                .title(course.getTitle())
                .subTitle(course.getSubTitle())
                .description(course.getDescription())
                .instructorId(course.getInstructorId())
                .instructorName(course.getInstructorName())
                .image(course.getImage())
                .mainTopic(course.getMainTopic())
                .subTopic(course.getSubTopic()).build();
    }

    public String updatePrice(String courseId, Integer Price) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        course.setPrice(Price);
        courseRepository.save(course);

        return "Thay đổi giá thành công";
    }

    public CourseDetailResponse getCourseDetail(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        String mainTopic =  topicService.getMainTopicById(course.getMainTopic());
        String subTopic = topicService.getSubTopicById(course.getSubTopic());

        List<SectionDetail> sections = sectionService.findSectionDetailByIds(course.getSections());

        return CourseDetailResponse.builder()
                .id(courseId)
                .image(course.getImage())
                .title(course.getTitle())
                .subTitle(course.getSubTitle())
                .description(course.getDescription())
                .instructorId(course.getInstructorId())
                .instructorName(course.getInstructorName())
                .mainTopic(mainTopic)
                .subTopic(subTopic)
                .language(course.getLanguage())
                .requirements(course.getRequirements())
                .targetAudiences(course.getTargetAudiences())
                .welcome(course.getWelcome())
                .congratulation(course.getCongratulation())
                .sections(sections)
                .build();
     }

}
