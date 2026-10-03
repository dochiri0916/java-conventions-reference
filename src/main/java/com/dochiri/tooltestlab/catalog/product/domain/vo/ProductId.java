package com.dochiri.tooltestlab.catalog.product.domain.vo;

import com.dochiri.tooltestlab.catalog.product.domain.exception.InvalidProductIdException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductIdRequiredException;
import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public final class ProductId {

    private final UUID value;

    private ProductId(final UUID value) {
        if (value == null) {
            throw new ProductIdRequiredException();
        }

        if (value.version() != 7) {
            throw new InvalidProductIdException(value.toString());
        }
        this.value = value;
    }

    public static ProductId generate() {
        return new ProductId(
                UuidCreator.getTimeOrderedEpoch()
        );
    }

    public static ProductId of(final UUID value) {
        return new ProductId(value);
    }

    public static ProductId parse(final String value) {
        if (value == null) {
            throw new ProductIdRequiredException();
        }

        final UUID uuid;
        try {
            uuid = UUID.fromString(value);
        } catch (final IllegalArgumentException exception) {
            throw new InvalidProductIdException(value);
        }

        if (!uuid.toString().equalsIgnoreCase(value)) {
            throw new InvalidProductIdException(value);
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
        if (!(object instanceof final ProductId other)) {
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
        return "ProductId[value=" + value + "]";
    }
}
