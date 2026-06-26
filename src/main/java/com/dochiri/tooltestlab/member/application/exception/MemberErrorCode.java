package com.dochiri.tooltestlab.member.application.exception;

import com.dochiri.tooltestlab.global.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum MemberErrorCode implements ErrorCode {

    LOGIN_FAILED(
            "LOGIN_FAILED",
            "이메일 또는 비밀번호가 일치하지 않습니다.",
            HttpStatus.UNAUTHORIZED
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    MemberErrorCode(String code, String message, HttpStatus httpStatus) {
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
