package com.dochiri.tooltestlab.order.application.exception;

public enum OrderApplicationErrorCode {

    ORDER_NOT_FOUND("주문 없음"),
    NOT_ORDER_OWNER("권한 없음"),
    EMPTY_CART("빈 장바구니");

    private final String message;

    OrderApplicationErrorCode(final String title) {
        this.message = title;
    }

    public String title() {
        return message;
    }
}
