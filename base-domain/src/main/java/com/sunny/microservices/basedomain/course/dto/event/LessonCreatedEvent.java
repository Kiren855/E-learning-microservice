package com.sunny.microservices.basedomain.course.dto.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonCreatedEvent {
    String lessonName;
    String username;
    String email;
}
