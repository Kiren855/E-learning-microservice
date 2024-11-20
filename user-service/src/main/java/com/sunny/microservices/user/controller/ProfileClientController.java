package com.sunny.microservices.user.controller;

import com.sunny.microservices.basedomain.user.dto.ProfileResponse;
import com.sunny.microservices.user.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequestMapping("/internal/")
public class ProfileClientController {
    ProfileService profileService;
    @GetMapping("/profile/{userId}")
    public ProfileResponse getProfile(@PathVariable String userId) {
        return profileService.profile(userId);
    }
}
