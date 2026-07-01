package com.dochiri.tooltestlab.product.application.exception;

public enum ProductErrorCode {

    PRODUCT_NOT_FOUND("상품 없음");

    private final String message;

    ProductErrorCode(final String title) {
        this.message = title;
    }

    public String title() {
        return message;
    }
}
