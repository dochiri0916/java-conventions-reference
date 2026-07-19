package com.dochiri.tooltestlab.catalog.product.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.exception.InvalidProductIdException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductIdRequiredException;

import java.util.UUID;

public record ProductId(String value) {

    public ProductId {
        if (value == null) {
            throw new ProductIdRequiredException();
        }

        value = value.strip();

        if (value.isBlank()) {
            throw new InvalidProductIdException(value);
        }
    }

    public static ProductId generate() {
        return new ProductId(
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
        );
    }

}
