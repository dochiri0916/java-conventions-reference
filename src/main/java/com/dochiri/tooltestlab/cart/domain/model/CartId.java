package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.CartDomainException;

import java.util.UUID;

public record CartId(String value) {

    public CartId {
        if (value == null) {
            throw CartDomainException.cartIdRequired();
        }
        final String stripped = value.strip();
        if (stripped.isBlank()) {
            throw CartDomainException.invalidCartId();
        }
        value = stripped;
    }

    public static CartId generate() {
        return new CartId(UUID.randomUUID().toString().replace("-", ""));
    }
}
