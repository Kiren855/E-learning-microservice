package com.sunny.microservices.course.dto.DTO;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonPreview  implements Serializable {
    String id;
    String name;
    String type;
    String type_id;
    Integer partNumber;
    Double duration;
}
