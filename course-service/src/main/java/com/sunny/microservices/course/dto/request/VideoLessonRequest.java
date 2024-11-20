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
    Integer partNumber;
    Double duration;
    MultipartFile videoFile;
    MultipartFile thumbnailFile;
}
