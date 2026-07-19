package com.dochiri.tooltestlab.catalog.product.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDomainException;

import java.util.UUID;

public record ProductId(String value) {

    public ProductId {
        if (value == null) {
            throw ProductDomainException.productIdRequired();
        }

        value = value.strip();

        if (value.isBlank()) {
            throw ProductDomainException.invalidProductId(value);
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
