package com.dochiri.tooltestlab.cart.domain.exception;

public final class QuantityRequiredException extends CartDomainException {

    public QuantityRequiredException() {
        super("수량은 필수입니다.");
    }

}
