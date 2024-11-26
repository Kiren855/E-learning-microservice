package com.sunny.microservices.course.dto.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionDetail {
    String id;
    String name;
    String partNumber;
    List<LessonDetail> lessons;
    Double duration;
}
