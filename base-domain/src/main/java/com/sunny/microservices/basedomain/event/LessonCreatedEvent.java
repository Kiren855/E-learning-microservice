package com.sunny.microservices.basedomain.event;

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
