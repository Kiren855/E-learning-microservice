package com.sunny.microservices.payment.service;


import com.sunny.microservices.basedomain.payment.dto.ResponseCode;
import com.sunny.microservices.payment.constant.DefaultValue;
import com.sunny.microservices.payment.exception.BusinessException;
import com.sunny.microservices.payment.util.EncodingUtil;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class CryptoService {
    private Mac mac;

    @Value("${payment.vnpay.secret-key}")
    private String secretKey;

    public CryptoService() {
        try {
            this.mac = Mac.getInstance(DefaultValue.HMAC_SHA512);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to initialize Mac instance", e);
        }
    }

    @PostConstruct
    void init() {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), DefaultValue.HMAC_SHA512);
            mac.init(secretKeySpec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Failed to initialize CryptoService", e);
        }
    }

    public String sign(String data) {
        try {
            return EncodingUtil.toHexString(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.VNPAY_SIGNING_FAILED);
        }
    }
}
