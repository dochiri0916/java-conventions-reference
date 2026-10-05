package com.dochiri.tooltestlab.review.domain.vo;

import com.dochiri.tooltestlab.review.domain.exception.InvalidProductReferenceIdException;
import com.dochiri.tooltestlab.review.domain.exception.ProductReferenceIdRequiredException;

import java.util.UUID;

public final class ProductReferenceId {

    private final UUID value;

    private ProductReferenceId(final UUID value) {
        if (value == null) {
            throw new ProductReferenceIdRequiredException();
        }

        if (value.version() != 7) {
            throw new InvalidProductReferenceIdException();
        }
        this.value = value;
    }

    public static ProductReferenceId of(final UUID value) {
        return new ProductReferenceId(value);
    }

    public static ProductReferenceId parse(final String value) {
        if (value == null) {
            throw new ProductReferenceIdRequiredException();
        }

        final UUID uuid;
        try {
            uuid = UUID.fromString(value);
        } catch (final IllegalArgumentException exception) {
            throw new InvalidProductReferenceIdException();
        }

        if (!uuid.toString()
                .equalsIgnoreCase(value)) {
            throw new InvalidProductReferenceIdException();
        }

        return of(uuid);
    }

    public UUID productId() {
        return value;
    }

    public String value() {
        return value.toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final ProductReferenceId other)) {
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
        return "ProductReferenceId[value=" + value + "]";
    }
}
