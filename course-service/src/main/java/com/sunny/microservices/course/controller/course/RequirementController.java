package com.sunny.microservices.course.controller.course;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.course.CourseRequirementRequest;
import com.sunny.microservices.course.service.course.RequirementService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RequirementController {

}
