package com.dochiri.tooltestlab.review.domain.model;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record ReviewId(String value) {

    public ReviewId {
        value = requireNonNull(value).strip();
        if (value.isBlank()) {
            throw new IllegalArgumentException("리뷰 ID는 공백일 수 없습니다.");
        }
    }

    public static ReviewId generate() {
        return new ReviewId(UUID.randomUUID().toString().replace("-", ""));
    }
}
