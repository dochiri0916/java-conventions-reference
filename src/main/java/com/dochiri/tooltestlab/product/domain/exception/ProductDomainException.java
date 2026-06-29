package com.dochiri.tooltestlab.product.domain.exception;

public abstract class ProductDomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductDomainErrorCode code;

    protected ProductDomainException(final ProductDomainErrorCode code, final String message) {
        super(message);
        this.code = code;
    }

    public ProductDomainErrorCode errorCode() {
        return code;
    }
}
