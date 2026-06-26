package com.dochiri.tooltestlab.order.application.exception;

import com.dochiri.tooltestlab.global.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum OrderApplicationErrorCode implements ErrorCode {

    ORDER_NOT_FOUND(
            "ORDER_NOT_FOUND",
            "해당 주문을 찾을 수 없습니다.",
            HttpStatus.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    OrderApplicationErrorCode(String code, String message, HttpStatus httpStatus) {
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
