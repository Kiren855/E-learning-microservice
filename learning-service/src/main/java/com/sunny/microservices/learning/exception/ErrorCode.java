package com.sunny.microservices.learning.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền", HttpStatus.FORBIDDEN),
    COURSE_NOT_FOUND(2001, "Không tìm thấy khoá học", HttpStatus.BAD_REQUEST),
    SECTION_NOT_FOUND(2002, "Không tìm thấy phần học", HttpStatus.BAD_REQUEST),
    LESSON_NOT_FOUND(2003, "Không tìm thấy bài học", HttpStatus.BAD_REQUEST),
    UPDATE_FAILED(2005, "Không thể cập nhật", HttpStatus.BAD_REQUEST),
    CREATE_FAILED(2004, "Không thể tạo mới", HttpStatus.BAD_REQUEST),
    DELETE_FAILED(2006, "Không thể xoá", HttpStatus.BAD_REQUEST),
   ;
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;
}
