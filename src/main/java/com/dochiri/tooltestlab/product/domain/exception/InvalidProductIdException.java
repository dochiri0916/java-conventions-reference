package com.dochiri.tooltestlab.product.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidProductIdException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductDomainErrorCode errorCode;

    private InvalidProductIdException(final ProductDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidProductIdException badProductId() {
        return new InvalidProductIdException(ProductDomainErrorCode.BAD_PRODUCT_ID);
    }

    public ProductDomainErrorCode code() {
        return errorCode;
    }
}
