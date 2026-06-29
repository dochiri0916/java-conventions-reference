package com.dochiri.tooltestlab.cart.domain.exception;

public final class CartItemLimitExceededException extends CartDomainException {

    public CartItemLimitExceededException(final int actualSize) {
        super(CartDomainErrorCode.TOO_MANY_ITEMS,
                "장바구니 상품은 최대 100개까지 가능합니다. 입력값: " + actualSize);
    }
}
