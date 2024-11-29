package com.sunny.microservices.basedomain.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RejectionCourseEvent {
    String username;
    String email;
    String courseName;
    String reason;
}
