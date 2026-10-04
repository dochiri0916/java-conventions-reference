package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.order.domain.exception.CartReferenceIdRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidCartReferenceIdException;
import java.util.UUID;

public final class CartReferenceId {
    private final UUID value;

    private CartReferenceId(final UUID value) {
        if (value == null) {
            throw new CartReferenceIdRequiredException();
        }
        if (value.version() != 7) {
            throw new InvalidCartReferenceIdException();
        }
        this.value = value;
    }

    public static CartReferenceId of(final UUID value) {
        return new CartReferenceId(value);
    }

    public static CartReferenceId parse(final String value) {
        if (value == null) {
            throw new CartReferenceIdRequiredException();
        }
        try {
            final UUID uuid = UUID.fromString(value);
            if (!uuid.toString().equalsIgnoreCase(value)) {
                throw new InvalidCartReferenceIdException();
            }
            return of(uuid);
        } catch (final IllegalArgumentException failure) {
            throw new InvalidCartReferenceIdException();
        }
    }

    public String value() {
        return value.toString();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof CartReferenceId id && value.equals(id.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
