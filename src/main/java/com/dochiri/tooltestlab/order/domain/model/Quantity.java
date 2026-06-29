package com.dochiri.tooltestlab.order.domain.model;

public record Quantity(int value) {

    public Quantity {
        if (value <= 0) {
            throw new com.dochiri.tooltestlab.order.domain.exception.InvalidQuantityException(value);
        }
    }

    public static Quantity from(final int value) {
        return new Quantity(value);
    }
}
