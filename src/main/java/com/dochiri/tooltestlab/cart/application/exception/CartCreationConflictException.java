package com.dochiri.tooltestlab.cart.application.exception;

public final class CartCreationConflictException extends CartApplicationException {

    public CartCreationConflictException(final Throwable cause) {
        super("장바구니가 동시에 생성되어 요청을 완료하지 못했습니다. 다시 시도해주세요.");
        initCause(cause);
    }
}
