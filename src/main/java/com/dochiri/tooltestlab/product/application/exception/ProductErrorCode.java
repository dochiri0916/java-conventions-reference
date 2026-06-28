package com.dochiri.tooltestlab.product.application.exception;

import com.dochiri.tooltestlab.global.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ProductErrorCode implements ErrorCode {

    PRODUCT_NOT_FOUND(
            "PRODUCT_NOT_FOUND",
            "해당 상품을 찾을 수 없습니다.",
            HttpStatus.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ProductErrorCode(String code, String message, HttpStatus httpStatus) {
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
