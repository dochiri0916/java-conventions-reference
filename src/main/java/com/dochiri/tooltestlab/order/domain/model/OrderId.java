package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException;
import com.dochiri.tooltestlab.order.domain.exception.OrderIdRequiredException;

import java.util.UUID;

public record OrderId(String value) {

    public OrderId {
        if (value == null) {
            throw new OrderIdRequiredException();
        }
        final String stripped = value.strip();
        if (stripped.isBlank()) {
            throw new InvalidOrderIdException();
        }
        value = stripped;
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID().toString().replace("-", ""));
    }
}
