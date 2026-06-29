package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidShippingAddressException extends OrderDomainException {

    public InvalidShippingAddressException(final String detail) {
        super(OrderDomainErrorCode.BAD_SHIPPING_ADDR, detail);
    }
}
