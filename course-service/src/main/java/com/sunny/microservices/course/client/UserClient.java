package com.sunny.microservices.course.client;

import com.sunny.microservices.course.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", url = "${user-service.url}")
public interface UserClient {
    @GetMapping("/internal/profile/{userId}")
    ProfileResponse getProfile(@PathVariable String userId);
}
