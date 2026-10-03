package com.dochiri.tooltestlab.global.domain.vo;

import com.dochiri.tooltestlab.global.domain.exception.InvalidQuantityException;

public final class Quantity {

    private final int value;

    private Quantity(final int value) {
        if (value <= 0) {
            throw new InvalidQuantityException();
        }
        this.value = value;
    }

    public static Quantity of(final int value) {
        return new Quantity(value);
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final Quantity other)) {
            return false;
        }
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return "Quantity[value=" + value + "]";
    }

}
