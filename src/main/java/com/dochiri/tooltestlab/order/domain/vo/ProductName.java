package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.order.domain.exception.ProductNameRequiredException;

public final class ProductName {

    private final String value;

    private ProductName(final String value) {
        if (value == null || value.isBlank()) {
            throw new ProductNameRequiredException();
        }
        final String normalized = value.strip();
        this.value = normalized;
    }

    public static ProductName of(final String value) {
        return new ProductName(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final ProductName other)) {
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
        return "ProductName[value=" + value + "]";
    }

}
