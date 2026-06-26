package com.dochiri.tooltestlab.global.error;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {

    VALIDATION_FAILED(
            "VALIDATION_FAILED",
            "요청 값이 올바르지 않습니다.",
            HttpStatus.BAD_REQUEST
    ),

    HTTP_MESSAGE_NOT_READABLE(
            "HTTP_MESSAGE_NOT_READABLE",
            "요청 본문을 읽을 수 없습니다.",
            HttpStatus.BAD_REQUEST
    ),

    MISSING_REQUEST_HEADER(
            "MISSING_REQUEST_HEADER",
            "필수 요청 헤더가 누락되었습니다.",
            HttpStatus.BAD_REQUEST
    ),

    TYPE_MISMATCH(
            "TYPE_MISMATCH",
            "요청 파라미터 타입이 올바르지 않습니다.",
            HttpStatus.BAD_REQUEST
    ),

    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "서버 내부 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    CommonErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

}
