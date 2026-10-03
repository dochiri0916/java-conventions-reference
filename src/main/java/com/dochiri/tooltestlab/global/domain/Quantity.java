package com.dochiri.tooltestlab.global.domain;

import com.dochiri.tooltestlab.global.domain.exception.InvalidQuantityException;

public record Quantity(int value) {

    public Quantity {
        if (value <= 0) {
            throw new InvalidQuantityException();
        }
    }

    public static Quantity of(final int value) {
        return new Quantity(value);
    }
}
