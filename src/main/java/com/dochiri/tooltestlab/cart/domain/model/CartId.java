package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartIdException;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record CartId(String value) {

    public CartId {
        requireNonNull(value, "장바구니 ID는 null일 수 없습니다.");
        final String stripped = value.strip();
        if (stripped.isBlank()) {
            throw new InvalidCartIdException();
        }
        value = stripped;
    }

    public static CartId generate() {
        return new CartId(UUID.randomUUID().toString().replace("-", ""));
    }
}
