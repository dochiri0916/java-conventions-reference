package com.dochiri.tooltestlab.catalog.product.domain.vo;

import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDescriptionRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDescriptionTooLongException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDescriptionTooShortException;

public final class ProductDescription {

    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 1_000;

    private final String value;

    private ProductDescription(final String value) {
        if (value == null) {
            throw new ProductDescriptionRequiredException();
        }

        final String normalized = value.strip();

        if (normalized.length() < MIN_LENGTH) {
            throw new ProductDescriptionTooShortException(MIN_LENGTH);
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new ProductDescriptionTooLongException(MAX_LENGTH);
        }
        this.value = normalized;
    }

    public static ProductDescription of(final String value) {
        return new ProductDescription(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final ProductDescription other)) {
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
        return "ProductDescription[value=" + value + "]";
    }

}
