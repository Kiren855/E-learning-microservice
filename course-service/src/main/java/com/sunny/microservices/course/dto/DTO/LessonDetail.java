package com.sunny.microservices.course.dto.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonDetail {
    String id;
    String name;
    String type;
    String type_id;
    Integer partNumber;
    Double duration;
}
