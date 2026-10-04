package com.dochiri.tooltestlab.order.domain.exception;

public final class CartReferenceIdRequiredException extends OrderDomainException {
    public CartReferenceIdRequiredException() {
        super("장바구니 참조 ID가 필요합니다.");
    }
}
