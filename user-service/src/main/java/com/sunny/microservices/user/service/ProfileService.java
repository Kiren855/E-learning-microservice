package com.sunny.microservices.user.service;

import com.sunny.microservices.user.dto.request.EditProfileRequest;
import com.sunny.microservices.user.dto.response.UserResponse;
import com.sunny.microservices.user.entity.User;
import com.sunny.microservices.user.mapper.UserMapper;
import com.sunny.microservices.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
        UserRepository userRepository;
        UserMapper userMapper;

        public UserResponse getProfile() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();

            User profile = userRepository.findByUserId(userId);


            return UserResponse.builder()
                    .username(profile.getUsername())
                    .firstName(profile.getFirstName())
                    .lastName(profile.getLastName())
                    .email(profile.getEmail())
                    .avatar(profile.getAvatar())
                    .dob(profile.getDob())
                    .introduce(profile.getIntroduce())
                    .googleId(profile.getGoogleId())
                    .userId(profile.getUserId()).build();
        }

        public String updateProfile(EditProfileRequest request) {
            Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            User profile = userRepository.findByUserId(userId);

            profile.setAvatar(request.getAvatar());
            profile.setFirstName(request.getFirstName());
            profile.setLastName(request.getLastName());
            profile.setDob(request.getDob());
            profile.setIntroduce(request.getIntroduce());

            userRepository.save(profile);

            return "cập nhật thông tin thành công";
        }
}
