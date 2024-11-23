package com.sunny.microservices.course.service.course;

import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.dto.request.course.CourseOverviewRequest;
import com.sunny.microservices.course.dto.response.course.CourseOverviewResponse;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseOverviewService {
    AzureFileStorageClient azureFileStorageClient;
    CourseRepository courseRepository;
    @Value("${azure.blob.doc-container}")
    @NonFinal
    String docContainer;

    public String updateOverviewCourse(String courseId, CourseOverviewRequest request) {
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
            return "Cập nhật thông tin tổng quan khóa học thành công";
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
                .instructor(course.getInstructor())
                .image(course.getImage())
                .mainTopic(course.getMainTopic())
                .subTopic(course.getSubTopic()).build();
    }
}
