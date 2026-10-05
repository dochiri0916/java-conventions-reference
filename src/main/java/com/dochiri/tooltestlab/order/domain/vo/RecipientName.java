package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;
import com.dochiri.tooltestlab.order.domain.exception.ShippingAddressRequiredException;

public final class RecipientName {

    private final String value;

    private RecipientName(final String value) {
        if (value == null) {
            throw new ShippingAddressRequiredException();
        }
        final String normalized = value.strip();
        if (normalized.isBlank()) {
            throw new InvalidShippingAddressException();
        }
        this.value = normalized;
    }

    public static RecipientName of(final String value) {
        return new RecipientName(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final RecipientName other)) {
            return false;
        }
        return value.equals(
                other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "RecipientName[value=" + value + "]";
    }

}
