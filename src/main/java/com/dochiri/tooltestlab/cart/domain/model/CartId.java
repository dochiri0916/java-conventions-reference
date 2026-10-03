package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.CartIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartIdException;

import java.util.UUID;

public record CartId(String value) {

    public CartId {
        if (value == null) {
            throw new CartIdRequiredException();
        }
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
