package com.dochiri.tooltestlab.product.domain.exception;

public final class InvalidStockException extends ProductDomainException {

    public InvalidStockException(final String detail) {
        super(ProductDomainErrorCode.INVALID_STOCK, detail);
    }
}
