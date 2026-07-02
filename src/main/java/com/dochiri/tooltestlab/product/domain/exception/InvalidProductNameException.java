package com.dochiri.tooltestlab.product.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidProductNameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductDomainErrorCode errorCode;

    private InvalidProductNameException(final ProductDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidProductNameException badProductName() {
        return new InvalidProductNameException(ProductDomainErrorCode.BAD_PRODUCT_NAME);
    }

    public static InvalidProductNameException required() {
        return new InvalidProductNameException(ProductDomainErrorCode.PRODUCT_NAME_REQUIRED);
    }

    public ProductDomainErrorCode code() {
        return errorCode;
    }
}
