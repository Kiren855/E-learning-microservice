package com.sunny.microservices.user.service;

import com.sunny.microservices.user.dto.identity.Credential;
import com.sunny.microservices.user.dto.identity.TokenExchangeParam;
import com.sunny.microservices.user.dto.identity.UserCreationParam;
import com.sunny.microservices.user.dto.request.LoginRequest;
import com.sunny.microservices.user.dto.request.RegistrationRequest;
import com.sunny.microservices.user.dto.request.TokenRequest;
import com.sunny.microservices.user.entity.Avatar;
import com.sunny.microservices.user.entity.User;
import com.sunny.microservices.user.exception.AppException;
import com.sunny.microservices.user.exception.ErrorCode;
import com.sunny.microservices.user.exception.ErrorNormalizer;
import com.sunny.microservices.user.repository.AvatarRepository;
import com.sunny.microservices.user.repository.IdentityClient;
import com.sunny.microservices.user.repository.UserRepository;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    UserRepository userRepository;
    IdentityClient identityClient;
    ErrorNormalizer errorNormalizer;
    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client-secret}")
    @NonFinal
    String clientSecret;

    public String register(RegistrationRequest request) {
        try {

            var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());

            log.info("token {}", token);
            var creationResponse = identityClient.createUser(
                    "Bearer " + token.getAccessToken(),
                    UserCreationParam.builder()
                            .username(request.getUsername())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .email(request.getEmail())
                            .enabled(true)
                            .emailVerified(false)
                            .credentials(List.of(Credential.builder()
                                    .type("password")
                                    .temporary(false)
                                    .value(request.getPassword())
                                    .build()))
                            .build());

            String userId = extractUserId(creationResponse);

            User profile = User.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .dob(request.getDob())
                    .build();

//            String firstLetter = request.getUsername().substring(0, 1).toUpperCase();
//            Avatar avatar = avatarRepository.findByName(firstLetter)
//                    .orElseThrow(()-> new AppException(ErrorCode.AVATAR_NOT_FOUND));

            profile.setUserId(userId);
            profile.setGoogleId("");
            profile.setAvatar("");
            profile.setIntroduce("chưa có thông tin gì để giới thiệu");
            userRepository.save(profile);

            return "Đăng ký user thành công";

        } catch (FeignException exception) {
            throw errorNormalizer.handleKeyCloakException(exception);
        }
    }

    public Object login(LoginRequest request){
        try {
            var clientToken = identityClient.exchangeToken(TokenExchangeParam.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());

            System.out.println("Client Token: " + clientToken);  // Log token

            var loginResponse = identityClient.login(
                    "Bearer " + clientToken.getAccessToken(),
                    TokenExchangeParam.builder()
                            .grant_type("password")
                            .client_id(clientId)
                            .client_secret(clientSecret)
                            .username(request.getEmail())
                            .password(request.getPassword())
                            .scope("openid")
                            .build()).getBody();

            return loginResponse;
        } catch (FeignException exception) {
            exception.printStackTrace();  // Log exception details
            throw new AppException(ErrorCode.ERROR_ACCOUNT);
        }

    }

    public Object refreshToken(TokenRequest request) {
        try{
            return identityClient.exchangeToken(TokenExchangeParam.builder()
                    .grant_type("refresh_token")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .refresh_token(request.getRefreshToken())
                    .build());

        }catch (FeignException exception){
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    public String logout(TokenRequest request){
       try{
           var clientToken = identityClient.exchangeToken(TokenExchangeParam.builder()
                   .grant_type("client_credentials")
                   .client_id(clientId)
                   .client_secret(clientSecret)
                   .scope("openid")
                   .build());

           identityClient.logout("Bearse " + clientToken.getAccessToken(),
                   TokenExchangeParam.builder()
                           .client_id(clientId)
                           .client_secret(clientSecret)
                           .refresh_token(request.getRefreshToken()).build());
           return "đăng xuất thành công";
       }catch (FeignException e){
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
       }
    }
    private String extractUserId(ResponseEntity<?> response) {
        String location = response.getHeaders().get("Location").getFirst();
        String[] splitedStr = location.split("/");
        return splitedStr[splitedStr.length - 1];
    }
}
