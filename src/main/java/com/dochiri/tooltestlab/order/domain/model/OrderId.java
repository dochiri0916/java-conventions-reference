package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;

import java.util.UUID;

public record OrderId(String value) {

    public OrderId {
        if (value == null) {
            throw OrderDomainException.orderIdRequired();
        }
        final String stripped = value.strip();
        if (stripped.isBlank()) {
            throw OrderDomainException.invalidOrderId();
        }
        value = stripped;
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID().toString().replace("-", ""));
    }
}
