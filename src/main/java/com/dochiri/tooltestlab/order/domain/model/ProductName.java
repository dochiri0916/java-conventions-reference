package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.ProductNameRequiredException;

public record ProductName(String value) {

    public ProductName {
        if (value == null || value.isBlank()) {
            throw new ProductNameRequiredException();
        }
        value = value.strip();
    }

}
