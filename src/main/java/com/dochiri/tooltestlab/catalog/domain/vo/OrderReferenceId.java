package com.dochiri.tooltestlab.catalog.domain.vo;

import java.util.Objects;
import java.util.UUID;

public final class OrderReferenceId {
    private final UUID value;

    private OrderReferenceId(final UUID value) {
        this.value = Objects.requireNonNull(value, "주문 참조 ID가 필요합니다.");
        if (value.version() != 7) {
            throw new IllegalArgumentException("주문 참조 ID는 UUID v7이어야 합니다.");
        }
    }

    public static OrderReferenceId of(final UUID value) {
        return new OrderReferenceId(value);
    }

    public static OrderReferenceId parse(final String value) {
        final UUID uuid = UUID.fromString(Objects.requireNonNull(value));
        if (!uuid.toString().equalsIgnoreCase(value)) {
            throw new IllegalArgumentException("유효하지 않은 주문 참조 ID입니다.");
        }
        return of(uuid);
    }

    public String value() {
        return value.toString();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof OrderReferenceId id && value.equals(id.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
