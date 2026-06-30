package com.dochiri.tooltestlab.product.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidStockException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductDomainErrorCode errorCode;
    private final String detailField;

    private InvalidStockException(final ProductDomainErrorCode errorCode, final String detail) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.detailField = requireNonNull(detail);
    }

    public static InvalidStockException invalidStock(final String detail) {
        return new InvalidStockException(ProductDomainErrorCode.INVALID_STOCK, detail);
    }

    public ProductDomainErrorCode code() {
        return errorCode;
    }

    public String detail() {
        return detailField;
    }
}
