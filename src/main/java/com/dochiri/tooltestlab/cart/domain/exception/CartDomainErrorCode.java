package com.dochiri.tooltestlab.cart.domain.exception;

public enum CartDomainErrorCode {

    INVALID_CART_ID("장바구니 ID는 빈 값일 수 없습니다."),
    INVALID_CART_ITEM("장바구니 상품 정보가 올바르지 않습니다."),
    INVALID_CART("장바구니 정보가 올바르지 않습니다."),
    TOO_MANY_ITEMS("장바구니 상품은 최대 100개까지 가능합니다.");

    private final String message;

    CartDomainErrorCode(final String message) {
        this.message = message;
    }

    public String title() {
        return message;
    }
}
