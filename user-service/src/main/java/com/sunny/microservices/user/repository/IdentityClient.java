package com.sunny.microservices.user.repository;

import com.sunny.microservices.user.dto.identity.TokenExchangeParam;
import com.sunny.microservices.user.dto.identity.TokenExchangeResponse;
import com.sunny.microservices.user.dto.identity.UserCreationParam;
import com.sunny.microservices.user.exception.CustomFeignErrorDecoder;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "identity-client", url = "${idp.url}", configuration = CustomFeignErrorDecoder.class)
public interface IdentityClient {
    @PostMapping(
            value = "/realms/sunny/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@QueryMap TokenExchangeParam param);

    @PostMapping(value = "/admin/realms/sunny/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(@RequestHeader("authorization") String token, @RequestBody UserCreationParam param);

    @PostMapping(value = "/realms/sunny/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<?> login(@RequestHeader("authorization") String token, @QueryMap TokenExchangeParam param);

    @PostMapping(value = "/realms/sunny /protocol/openid-connect/logout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void logout(@RequestHeader("authorization") String token, @QueryMap TokenExchangeParam param);
}