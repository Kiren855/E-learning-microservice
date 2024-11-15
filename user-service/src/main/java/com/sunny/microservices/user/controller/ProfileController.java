package com.sunny.microservices.user.controller;

import com.sunny.microservices.user.dto.ApiResponse;
import com.sunny.microservices.user.dto.request.EditProfileRequest;
import com.sunny.microservices.user.dto.response.UserResponse;
import com.sunny.microservices.user.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileController {
    ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .result(profileService.getProfile())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(EditProfileRequest request) {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .message(profileService.updateProfile(request))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
