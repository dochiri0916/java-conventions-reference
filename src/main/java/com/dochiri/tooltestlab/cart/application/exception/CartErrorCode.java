package com.dochiri.tooltestlab.cart.application.exception;

import com.dochiri.tooltestlab.global.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum CartErrorCode implements ErrorCode {

    CART_NOT_FOUND(
            "CART_NOT_FOUND",
            "해당 장바구니를 찾을 수 없습니다.",
            HttpStatus.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    CartErrorCode(String code, String message, HttpStatus httpStatus) {
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
