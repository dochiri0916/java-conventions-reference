package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;

public record Quantity(int value) {

    public Quantity {
        if (value <= 0) {
            throw OrderDomainException.invalidQuantity(value);
        }
    }

    public static Quantity from(final int value) {
        return new Quantity(value);
    }
}
