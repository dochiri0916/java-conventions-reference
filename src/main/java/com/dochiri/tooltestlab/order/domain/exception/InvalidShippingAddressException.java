package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidShippingAddressException extends OrderDomainException {

    public InvalidShippingAddressException() {
        super("배송지 정보가 올바르지 않습니다.");
    }

}
