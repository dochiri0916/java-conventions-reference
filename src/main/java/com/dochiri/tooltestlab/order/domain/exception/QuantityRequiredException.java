package com.dochiri.tooltestlab.order.domain.exception;

public final class QuantityRequiredException extends OrderDomainException {

    public QuantityRequiredException() {
        super("수량은 필수입니다.");
    }

}
