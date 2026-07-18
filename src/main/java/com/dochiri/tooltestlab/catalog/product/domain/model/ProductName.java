package com.dochiri.tooltestlab.catalog.product.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.exception.InvalidProductNameException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductNameRequiredException;

public record ProductName(String value) {

    public ProductName {
        if (value == null) {
            throw new ProductNameRequiredException();
        }

        final String normalized = value.strip();

        if (normalized.isBlank()) {
            throw new InvalidProductNameException(value);
        }

        value = normalized;
    }

}