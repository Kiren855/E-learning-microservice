package com.sunny.microservices.course.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoLessonRequest {
    String name;
    String type;
    Integer partNumber;

    MultipartFile videoFile;
    MultipartFile thumbnailFile;
}
