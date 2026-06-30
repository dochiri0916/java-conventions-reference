package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record OrderId(String value) {

    public OrderId {
        requireNonNull(value, "주문 ID는 null일 수 없습니다.");
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
