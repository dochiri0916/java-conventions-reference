package com.dochiri.tooltestlab.category.domain.model;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record CategoryId(String value) {

    public CategoryId {
        value = requireNonNull(value).strip();
        if (value.isBlank()) {
            throw new IllegalArgumentException("카테고리 ID는 공백일 수 없습니다.");
        }
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID().toString().replace("-", ""));
    }
}
