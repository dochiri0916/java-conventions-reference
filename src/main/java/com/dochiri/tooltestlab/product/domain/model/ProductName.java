package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InvalidProductNameException;

import static java.util.Objects.requireNonNull;

public record ProductName(String value) {

    public ProductName {
        requireNonNull(value, "상품명은 null일 수 없습니다.");
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidProductNameException.badProductName();
        }
    }
}
