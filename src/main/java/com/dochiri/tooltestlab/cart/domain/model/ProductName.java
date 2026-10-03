package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.ProductNameRequiredException;

public record ProductName(String value) {

    public ProductName {
        if (value == null || value.isBlank()) {
            throw new ProductNameRequiredException();
        }
        value = value.strip();
    }

}
