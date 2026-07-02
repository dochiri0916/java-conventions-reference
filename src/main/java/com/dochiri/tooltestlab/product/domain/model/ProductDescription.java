package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InvalidProductException;

public record ProductDescription(String value) {

    public ProductDescription {
        if (value == null) {
            throw InvalidProductException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidProductException.invalidProduct();
        }
    }
}
