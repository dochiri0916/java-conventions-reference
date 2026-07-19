package com.dochiri.tooltestlab.catalog.product.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDomainException;

public record ProductDescription(String value) {

    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 1_000;

    public ProductDescription {
        if (value == null) {
            throw ProductDomainException.productDescriptionRequired();
        }

        value = value.strip();

        if (value.length() < MIN_LENGTH) {
            throw ProductDomainException.productDescriptionTooShort(MIN_LENGTH);
        }

        if (value.length() > MAX_LENGTH) {
            throw ProductDomainException.productDescriptionTooLong(MAX_LENGTH);
        }
    }

}
