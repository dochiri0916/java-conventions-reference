package com.dochiri.tooltestlab.product.application.exception;

public enum ProductErrorCode {

    PRODUCT_NOT_FOUND("상품 없음");

    private final String title;

    ProductErrorCode(final String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
