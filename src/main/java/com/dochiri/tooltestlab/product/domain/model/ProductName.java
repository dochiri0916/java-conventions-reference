package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InvalidProductNameException;

public record ProductName(String value) {

    public ProductName {
        if (value == null) {
            throw InvalidProductNameException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidProductNameException.badProductName();
        }
    }
}
