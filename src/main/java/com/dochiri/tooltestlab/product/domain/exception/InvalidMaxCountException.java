package com.dochiri.tooltestlab.product.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidMaxCountException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductDomainErrorCode errorCode;

    private InvalidMaxCountException(final ProductDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidMaxCountException notPositive(final int value) {
        return new InvalidMaxCountException(ProductDomainErrorCode.MAX_COUNT_NOT_POSITIVE);
    }

    public ProductDomainErrorCode code() {
        return errorCode;
    }
}
