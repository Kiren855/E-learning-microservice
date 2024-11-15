package com.sunny.microservices.user.controller;

import com.sunny.microservices.user.dto.ApiResponse;
import com.sunny.microservices.user.dto.identity.TokenExchangeResponse;
import com.sunny.microservices.user.dto.request.LoginRequest;
import com.sunny.microservices.user.dto.request.RegistrationRequest;
import com.sunny.microservices.user.dto.request.TokenRequest;
import com.sunny.microservices.user.dto.response.UserResponse;
import com.sunny.microservices.user.exception.AppException;
import com.sunny.microservices.user.exception.ErrorCode;
import com.sunny.microservices.user.service.IdentityService;
import jakarta.validation.Valid;
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
@RequestMapping("/auth")
public class IdentityController {
    IdentityService identityService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .message(identityService.register(request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try{
            ApiResponse<?> response = ApiResponse.builder()
                    .message("Đăng nhập thành công")
                    .result(identityService.login(request))
                    .build();
            return  ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            throw new AppException(ErrorCode.ERROR_ACCOUNT);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRequest request){
        ApiResponse<?> response = ApiResponse.builder()
                .message("phục hồi accress token thành công")
                .result(identityService.refreshToken(request))
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .message(identityService.logout(request))
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
