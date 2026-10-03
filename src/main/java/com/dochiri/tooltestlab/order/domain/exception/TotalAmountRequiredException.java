package com.dochiri.tooltestlab.order.domain.exception;

public final class TotalAmountRequiredException extends OrderDomainException {

    public TotalAmountRequiredException() {
        super("총 금액은 필수입니다.");
    }

}
