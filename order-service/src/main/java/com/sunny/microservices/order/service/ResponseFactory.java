package com.sunny.microservices.order.service;


import com.sunny.microservices.basedomain.payment.dto.ApiError;
import com.sunny.microservices.basedomain.payment.dto.Meta;
import com.sunny.microservices.basedomain.payment.dto.ResponseCode;
import com.sunny.microservices.basedomain.payment.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ResponseFactory {
    @Value("payment")
    String appName;


    public ResponseDto success(ResponseCode responseCode) {
        var meta = Meta.builder()
                .status(responseCode.getType())
                .serviceId(appName)
                .build();

        return new ResponseDto(meta, null);
    }

    public ResponseDto success(Object payload) {
        var meta = Meta.builder()
                .status(ResponseCode.SUCCESS.getType())
                .serviceId(appName)
                .build();

        return new ResponseDto(meta, payload);
    }


    public ResponseDto invalidParams(Collection<ApiError> errors) {
        var meta = Meta.builder()
                .status(ResponseCode.INVALID_PARAMS.getType())
                .serviceId(appName)
                .errors(errors)
                .build();

        return new ResponseDto(meta, null);
    }
}
