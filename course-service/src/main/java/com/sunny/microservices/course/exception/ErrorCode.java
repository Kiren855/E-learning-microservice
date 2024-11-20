package com.sunny.microservices.course.exception;

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
    LESSON_NOT_EMPTY(2007, "Có bài học tồn tại trong phần học này", HttpStatus.BAD_REQUEST),
    SECTION_NOT_EMPTY(2008, "Có phần học tồn tại trong khoá học này", HttpStatus.BAD_REQUEST),
    COURSE_CANNOT_DELETE(2009,"Khoá học này đang được thương mại, không thể xoá", HttpStatus.BAD_REQUEST),
    FILE_CANNOT_UPLOAD(2010, "Không thể upload file lên hệ thống", HttpStatus.BAD_REQUEST),
    FILE_INVALID(2011, "File không hợp lệ", HttpStatus.BAD_REQUEST),
    TITLE_REQUIRED(2012, "Cần phải có title", HttpStatus.BAD_REQUEST),
    SUB_TITLE_REQUIRED(2013, "Cần phải có sub title", HttpStatus.BAD_REQUEST),
    CONTENT_REQUIRED(2014, "Cần có nội dung content", HttpStatus.BAD_REQUEST),
    QUESTION_REQUIRED(2015, "Cần có thông tin về câu hỏi", HttpStatus.BAD_REQUEST),
    OPTION_REQUIRED(2016, "Cần phải có nội dung cho câu hỏi", HttpStatus.BAD_REQUEST),
    NAME_REQUIRED(2017, "Cần nhập tên phần này", HttpStatus.BAD_REQUEST),
    PART_NUMBER_REQUIRED(2018, "Cần nhập số thứ tự", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(2019, "Không tìm thấy file cần xoá trên cloud", HttpStatus.BAD_REQUEST),
    FILE_CANNOT_DELETE(2020, "Không thể xoá file khỏi hệ thống", HttpStatus.BAD_REQUEST),
    VIDEO_NOT_FOUND(2021, "Không thể tìm thấy bài giảng Video", HttpStatus.BAD_REQUEST),
    DOC_NOT_FOUND(2022, "Không thể tìm thấy tài liệu bài giảng", HttpStatus.BAD_REQUEST),
    EXAM_NOT_FOUND(2023, "Không thể tìm thấy bài kiểm tra", HttpStatus.BAD_REQUEST)
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
