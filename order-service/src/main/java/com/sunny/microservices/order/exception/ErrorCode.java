package com.sunny.microservices.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền", HttpStatus.FORBIDDEN),
    COURSE_EXISTS(4001, "Khoá học đã có trong danh sách", HttpStatus.BAD_REQUEST),
    COURSE_NOT_EXISTS(4002, "Khoá học không tồn tại", HttpStatus.BAD_REQUEST),
    REQUEST_ID_BLANK(4003, "request id đang có khoảng chống", HttpStatus.BAD_REQUEST),
    USERID_CANNOT_NULL(4004, "user id không được để trống", HttpStatus.BAD_REQUEST),
    COURSEID_CANNOT_NULL(4005, "course id không được để trống", HttpStatus.BAD_REQUEST);
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;
}
