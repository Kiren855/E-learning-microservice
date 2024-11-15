package com.sunny.microservices.course.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseRequest {
    MultipartFile image;
    @NotEmpty(message = "TITLE_REQUIRED")
    String title;
    @NotEmpty(message = "SUB_TITLE_REQUIRED")
    String subTitle;
    String description;
    List<String> topic;
    List<String> language;
    Double price;
    List<String> targetAudiences;
    List<String> requirements;
}
