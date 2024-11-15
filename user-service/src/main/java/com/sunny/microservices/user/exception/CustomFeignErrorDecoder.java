package com.sunny.microservices.user.exception;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomFeignErrorDecoder implements ErrorDecoder {
    private final ErrorNormalizer errorNormalizer;

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response.status() == 400) {
                FeignException exception = FeignException.errorStatus(methodKey, response);
                // Xử lý lỗi từ FeignException và trả về AppException thay vì ném FeignException.
                return errorNormalizer.handleKeyCloakException(exception);
            }
        } catch (Exception e) {
            // Đảm bảo lỗi không được ném ra ngoài mà được xử lý hoàn toàn
            return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        // Trả về lỗi mặc định nếu không phải 400 hoặc không xử lý được.
        return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
}