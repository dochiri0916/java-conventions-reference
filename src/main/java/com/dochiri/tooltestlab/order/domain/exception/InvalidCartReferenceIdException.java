package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidCartReferenceIdException extends OrderDomainException {
    public InvalidCartReferenceIdException() {
        super("유효하지 않은 장바구니 참조 ID입니다.");
    }
}
