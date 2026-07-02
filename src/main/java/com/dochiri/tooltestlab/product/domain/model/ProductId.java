package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException;

import java.util.UUID;

public record ProductId(String value) {

    public ProductId {
        if (value == null) {
            throw InvalidProductIdException.required();
        }
        final String stripped = value.strip();
        if (stripped.isBlank()) {
            throw InvalidProductIdException.badProductId();
        }
        value = stripped;
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID().toString().replace("-", ""));
    }
}
