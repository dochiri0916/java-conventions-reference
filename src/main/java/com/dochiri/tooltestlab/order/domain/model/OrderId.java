package com.dochiri.tooltestlab.order.domain.model;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * 주문 식별자 Value Object.
 */
public record OrderId(String value) {

    public OrderId {
        requireNonNull(value, "주문 ID는 null일 수 없습니다.");
        if (value.isBlank()) throw new IllegalArgumentException("주문 ID는 빈 값일 수 없습니다.");
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID().toString().replace("-", ""));
    }
}
