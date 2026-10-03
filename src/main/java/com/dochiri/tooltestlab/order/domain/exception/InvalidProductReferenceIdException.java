package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidProductReferenceIdException extends OrderDomainException {

    public InvalidProductReferenceIdException() {
        super("유효하지 않은 상품 참조 ID입니다.");
    }
}
