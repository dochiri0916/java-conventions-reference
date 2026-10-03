package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;
import com.dochiri.tooltestlab.order.domain.exception.ShippingAddressRequiredException;

public final class ZipCode {

    private final String value;

    private ZipCode(final String value) {
        if (value == null) {
            throw new ShippingAddressRequiredException();
        }
        final String normalized = value.strip();
        if (normalized.isBlank()) {
            throw new InvalidShippingAddressException();
        }
        this.value = normalized;
    }

    public static ZipCode of(final String value) {
        return new ZipCode(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final ZipCode other)) {
            return false;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "ZipCode[value=" + value + "]";
    }

}
