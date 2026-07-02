package com.dochiri.tooltestlab.product.application.exception;

public enum ProductErrorCode {

    PRODUCT_NOT_FOUND("상품 없음"),
    INVALID_INPUT("입력값이 올바르지 않습니다.");

    private final String message;

    ProductErrorCode(final String title) {
        this.message = title;
    }

    public String title() {
        return message;
    }
}
