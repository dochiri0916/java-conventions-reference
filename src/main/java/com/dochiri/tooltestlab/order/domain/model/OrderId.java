package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException;

import java.util.UUID;

public record OrderId(String value) {

    public OrderId {
        if (value == null) {
            throw InvalidOrderIdException.required();
        }
        final String stripped = value.strip();
        if (stripped.isBlank()) {
            throw InvalidOrderIdException.invalidOrderId();
        }
        value = stripped;
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID().toString().replace("-", ""));
    }
}
