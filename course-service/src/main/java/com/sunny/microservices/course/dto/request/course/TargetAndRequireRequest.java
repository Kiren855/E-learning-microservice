package com.sunny.microservices.course.dto.request.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TargetAndRequireRequest {
    List<String> targetAudiences;
    List<String> requirements;
}
