package com.dochiri.tooltestlab.product.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidMaxCountException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductDomainErrorCode errorCode;
    private final int invalidMaxCount;

    private InvalidMaxCountException(final ProductDomainErrorCode errorCode, final int value) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.invalidMaxCount = value;
    }

    public static InvalidMaxCountException notPositive(final int value) {
        return new InvalidMaxCountException(ProductDomainErrorCode.MAX_COUNT_NOT_POSITIVE, value);
    }

    public ProductDomainErrorCode code() {
        return errorCode;
    }

    public int value() {
        return invalidMaxCount;
    }
}
