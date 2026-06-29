package com.dochiri.tooltestlab.cart.domain.model;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record CartId(String value) {

    public CartId {
        requireNonNull(value, "장바구니 ID는 null일 수 없습니다.");
        String stripped = value.strip();
        if (stripped.isBlank()) throw new IllegalArgumentException("장바구니 ID는 빈 값일 수 없습니다.");
        value = stripped;
    }

    public static CartId generate() {
        return new CartId(UUID.randomUUID().toString().replace("-", ""));
    }
}
