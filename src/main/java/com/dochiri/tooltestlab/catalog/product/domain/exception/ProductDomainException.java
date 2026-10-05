package com.dochiri.tooltestlab.catalog.product.domain.exception;

public abstract sealed class ProductDomainException extends RuntimeException
        permits InsufficientStockException,
        InvalidCategoryIdsException,
        InvalidProductIdException,
        InvalidProductNameException,
        InvalidStockQuantityException,
        ProductCategoryIdsRequiredException,
        ProductDescriptionRequiredException,
        ProductDescriptionTooLongException,
        ProductDescriptionTooShortException,
        ProductIdRequiredException,
        ProductNameRequiredException,
        ProductPriceRequiredException,
        ProductStatusRequiredException,
        ProductStockRequiredException,
        StatusTransitionNotAllowedException,
        StockOverflowException {

    protected ProductDomainException(final String message) {
        super(message);
    }
}
