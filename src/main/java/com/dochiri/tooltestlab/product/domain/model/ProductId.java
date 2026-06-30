package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record ProductId(String value) {

    public ProductId {
        requireNonNull(value, "상품 ID는 null일 수 없습니다.");
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
