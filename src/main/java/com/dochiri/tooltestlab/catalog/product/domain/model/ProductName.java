package com.dochiri.tooltestlab.catalog.product.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDomainException;

public record ProductName(String value) {

    public ProductName {
        if (value == null) {
            throw ProductDomainException.productNameRequired();
        }

        final String normalized = value.strip();

        if (normalized.isBlank()) {
            throw ProductDomainException.invalidProductName(value);
        }

        value = normalized;
    }

}
