package com.dochiri.tooltestlab.product.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidProductException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductDomainErrorCode errorCode;

    private InvalidProductException(final ProductDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidProductException invalidProduct() {
        return new InvalidProductException(ProductDomainErrorCode.INVALID_PRODUCT);
    }

    public ProductDomainErrorCode code() {
        return errorCode;
    }
}
