package com.dochiri.tooltestlab.product.domain.model;

import static java.util.Objects.requireNonNull;

public record ProductDescription(String value) {

    public ProductDescription {
        requireNonNull(value, "상품 설명은 null일 수 없습니다.");
        value = value.strip();
    }
}
