package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.ProductReferenceIdRequiredException;

public record ProductReferenceId(String value) {

    public ProductReferenceId {
        if (value == null || value.isBlank()) {
            throw new ProductReferenceIdRequiredException();
        }
        value = value.strip();
    }

}
