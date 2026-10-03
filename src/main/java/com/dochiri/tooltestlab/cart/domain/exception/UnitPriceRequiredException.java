package com.dochiri.tooltestlab.cart.domain.exception;

public final class UnitPriceRequiredException extends CartDomainException {

    public UnitPriceRequiredException() {
        super("단가는 필수입니다.");
    }

}
