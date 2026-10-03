package com.dochiri.tooltestlab.order.domain.exception;

public final class UnitPriceRequiredException extends OrderDomainException {

    public UnitPriceRequiredException() {
        super("단가는 필수입니다.");
    }

}
