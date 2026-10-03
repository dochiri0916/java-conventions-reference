package com.dochiri.tooltestlab.cart.domain.vo;

import com.dochiri.tooltestlab.cart.domain.exception.CartIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartIdException;
import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public final class CartId {

    private final UUID value;

    private CartId(final UUID value) {
        if (value == null) {
            throw new CartIdRequiredException();
        }

        if (value.version() != 7) {
            throw new InvalidCartIdException();
        }
        this.value = value;
    }

    public static CartId generate() {
        return new CartId(
                UuidCreator.getTimeOrderedEpoch()
        );
    }

    public static CartId of(final UUID value) {
        return new CartId(value);
    }

    public static CartId parse(final String value) {
        if (value == null) {
            throw new CartIdRequiredException();
        }

        final UUID uuid;
        try {
            uuid = UUID.fromString(value);
        } catch (final IllegalArgumentException exception) {
            throw new InvalidCartIdException();
        }

        if (!uuid.toString().equalsIgnoreCase(value)) {
            throw new InvalidCartIdException();
        }

        return of(uuid);
    }

    public UUID cartId() {
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
        if (!(object instanceof final CartId other)) {
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
        return "CartId[value=" + value + "]";
    }
}
