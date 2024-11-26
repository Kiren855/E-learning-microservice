package com.sunny.microservices.basedomain.course.dto.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionLearning {
    String name;
    List<LessonLearning> lessons;
    Double duration;
}
