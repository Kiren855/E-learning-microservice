package com.sunny.microservices.user.service;

import com.sunny.microservices.basedomain.user.dto.ProfileResponse;
import com.sunny.microservices.user.client.AzureFileStorageClient;
import com.sunny.microservices.user.dto.request.EditProfileRequest;
import com.sunny.microservices.user.dto.response.UserResponse;
import com.sunny.microservices.user.entity.User;
import com.sunny.microservices.user.exception.AppException;
import com.sunny.microservices.user.exception.ErrorCode;
import com.sunny.microservices.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
        UserRepository userRepository;
        AzureFileStorageClient azureFileStorageClient;

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
            try{
                Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
                String userId = authentication.getName();
                User profile = userRepository.findByUserId(userId);

                if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                    profile.setFirstName(request.getFirstName());
                }
                if (request.getAvatar() != null && request.getAvatar().getOriginalFilename() != null) {
                    String pathAvatar = azureFileStorageClient.uploadFile(
                            "avatar-container",
                            Objects.requireNonNull(request.getAvatar().getOriginalFilename()),
                            request.getAvatar().getInputStream(),
                            request.getAvatar().getSize()
                    );
                    profile.setAvatar(pathAvatar);
                }
                if (request.getLastName() != null && !request.getLastName().isEmpty()) {
                    profile.setLastName(request.getLastName());
                }
                if (request.getDob() != null) {
                    profile.setDob(request.getDob());
                }
                if (request.getIntroduce() != null && !request.getIntroduce().isEmpty()) {
                    profile.setIntroduce(request.getIntroduce());
                }
                userRepository.save(profile);

                return "cập nhật thông tin thành công";
            }catch (IOException e) {
                throw new AppException(ErrorCode.FILE_CANNOT_UPLOAD);
            }
        }

        public ProfileResponse profile(String userId){
            User user = userRepository.findByUserId(userId);

            return ProfileResponse.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName()).build();
        }
}
