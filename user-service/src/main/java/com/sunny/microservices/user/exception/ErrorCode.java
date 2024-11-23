package com.sunny.microservices.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    EMAIL_EXISTED(1008, "Email existed, please choose another one", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1009, "Username existed, please choose another one", HttpStatus.BAD_REQUEST),
    USERNAME_IS_MISSING(1010, "Please enter username", HttpStatus.BAD_REQUEST),
    INVALID_AGE(1011, "user must be at least {min} and max {max} age", HttpStatus.BAD_REQUEST),
    USERNAME_REQUIRED(1012, "người dùng cần nhập username", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(1013, "người dùng cần nhập password", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED(1014, "người dùng cần nhập email", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1015, "email không hợp lệ", HttpStatus.BAD_REQUEST),
    FIRSTNAME_REQUIRED(1016, "người dùng cần nhập tên", HttpStatus.BAD_REQUEST),
    LASTNAME_REQUIRED(1017, "người dùng cần nhập họ", HttpStatus.BAD_REQUEST),
    ERROR_ACCOUNT(1018, "thông tin tài khoản hoặc mật khẩu không đúng", HttpStatus.BAD_REQUEST),
    INVALID_FIRSTNAME(1019, "tên người dùng phải lớn hơn {min} kí tự", HttpStatus.BAD_REQUEST),
    INVALID_LASTNAME(1020, "họ người dùng phải lớn hơn {min} kí tự", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1021, "token đã hết hạn, không thể sử dụng được nữa", HttpStatus.BAD_REQUEST),
    USERNAME_MUST_START_WITH_LETTER(1022, "username kí tự đầu phải là kí tự a-z", HttpStatus.BAD_REQUEST),
    AVATAR_NOT_FOUND(1023, "Không tìm thấy avatar", HttpStatus.BAD_REQUEST),
    FILE_CANNOT_UPLOAD(1024, "Không thể upload file", HttpStatus.BAD_REQUEST);
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;
}
