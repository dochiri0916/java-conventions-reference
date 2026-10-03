package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.ProductReferenceIdRequiredException;

public record ProductReferenceId(String value) {

    public ProductReferenceId {
        if (value == null || value.isBlank()) {
            throw new ProductReferenceIdRequiredException();
        }
        value = value.strip();
    }

}
