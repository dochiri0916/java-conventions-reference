package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InvalidProductException;

import static java.util.Objects.requireNonNull;

public record ProductDescription(String value) {

    public ProductDescription {
        requireNonNull(value, "상품 설명은 null일 수 없습니다.");
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidProductException.invalidProduct();
        }
    }
}
