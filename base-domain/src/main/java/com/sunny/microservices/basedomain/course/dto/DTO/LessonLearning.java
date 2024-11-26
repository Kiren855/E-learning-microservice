package com.sunny.microservices.basedomain.course.dto.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonLearning {
    String id;
    String name;
    String type;
    String type_id;
    Integer partNumber;
    Double duration;
}
