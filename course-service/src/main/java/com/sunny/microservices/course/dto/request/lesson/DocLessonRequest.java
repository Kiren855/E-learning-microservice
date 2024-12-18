package com.sunny.microservices.course.dto.request.lesson;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocLessonRequest {
    String name;
    Integer partNumber;
    MultipartFile docFile;
}
