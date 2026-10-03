package com.dochiri.tooltestlab.catalog.product.application.exception;

public abstract sealed class ProductApplicationException extends RuntimeException
        permits ProductNotFoundException,
        DuplicateProductNameException,
        CategoriesNotFoundException,
        CategoriesNotActiveException,
        InsufficientProductStockException,
        ProductStockOverflowException {
    protected ProductApplicationException(final String message) {
        super(message);
    }

}
