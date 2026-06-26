package com.dochiri.tooltestlab.order.domain.exception;

import com.dochiri.tooltestlab.global.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum OrderDomainErrorCode implements ErrorCode {

    INVALID_ORDER_STATUS(
            "INVALID_ORDER_STATUS",
            "현재 주문 상태에서는 처리할 수 없습니다.",
            HttpStatus.BAD_REQUEST
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    OrderDomainErrorCode(String code, String message, HttpStatus httpStatus) {
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
