package com.dochiri.tooltestlab.order.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidShippingAddressException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderDomainErrorCode errorCode;
    private final String detailField;

    private InvalidShippingAddressException(final OrderDomainErrorCode errorCode, final String detail) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.detailField = requireNonNull(detail);
    }

    public static InvalidShippingAddressException badShippingAddr(final String detail) {
        return new InvalidShippingAddressException(OrderDomainErrorCode.BAD_SHIPPING_ADDR, detail);
    }

    public OrderDomainErrorCode code() {
        return errorCode;
    }

    public String detail() {
        return detailField;
    }
}
