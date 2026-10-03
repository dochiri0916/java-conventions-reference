package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException;
import com.dochiri.tooltestlab.order.domain.exception.OrderIdRequiredException;
import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public final class OrderId {

    private final UUID value;

    private OrderId(final UUID value) {
        if (value == null) {
            throw new OrderIdRequiredException();
        }

        if (value.version() != 7) {
            throw new InvalidOrderIdException();
        }
        this.value = value;
    }

    public static OrderId generate() {
        return new OrderId(
                UuidCreator.getTimeOrderedEpoch()
        );
    }

    public static OrderId of(final UUID value) {
        return new OrderId(value);
    }

    public static OrderId parse(final String value) {
        if (value == null) {
            throw new OrderIdRequiredException();
        }

        final UUID uuid;
        try {
            uuid = UUID.fromString(value);
        } catch (final IllegalArgumentException exception) {
            throw new InvalidOrderIdException();
        }

        if (!uuid.toString().equalsIgnoreCase(value)) {
            throw new InvalidOrderIdException();
        }

        return of(uuid);
    }

    public UUID orderId() {
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
        if (!(object instanceof final OrderId other)) {
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
        return "OrderId[value=" + value + "]";
    }
}
