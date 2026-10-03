package com.dochiri.tooltestlab.cart.domain.exception;

public final class InvalidProductReferenceIdException extends CartDomainException {

    public InvalidProductReferenceIdException() {
        super("유효하지 않은 상품 참조 ID입니다.");
    }
}
