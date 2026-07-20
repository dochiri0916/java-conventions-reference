package com.dochiri.tooltestlab.order.application.exception;

import com.dochiri.tooltestlab.global.exception.ErrorCode;
import com.dochiri.tooltestlab.global.exception.ErrorKind;

public enum OrderApplicationErrorCode implements ErrorCode {

    ORDER_NOT_FOUND("ORDER_NOT_FOUND", ErrorKind.NOT_FOUND, "주문을 찾을 수 없습니다."),
    NOT_ORDER_OWNER("NOT_ORDER_OWNER", ErrorKind.FORBIDDEN, "주문을 변경할 권한이 없습니다."),
    EMPTY_CART("EMPTY_CART", ErrorKind.INVALID_STATE, "장바구니에 담긴 상품이 없습니다.");

    private final String code;
    private final ErrorKind kind;
    private final String detail;

    OrderApplicationErrorCode(final String code, final ErrorKind kind, final String detail) {
        this.code = code;
        this.kind = kind;
        this.detail = detail;
    }

    @Override public String code() { return code; }
    @Override public ErrorKind kind() { return kind; }
    @Override public String detail() { return detail; }
}
