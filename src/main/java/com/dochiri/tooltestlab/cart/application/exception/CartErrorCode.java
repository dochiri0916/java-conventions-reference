package com.dochiri.tooltestlab.cart.application.exception;

public enum CartErrorCode {

    CART_NOT_FOUND("장바구니 없음"),
    CART_ITEM_NOT_FOUND("장바구니 상품 없음"),
    NOT_CART_OWNER("권한 없음");

    private final String title;

    CartErrorCode(final String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
