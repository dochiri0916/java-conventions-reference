package com.dochiri.tooltestlab.order.domain.model;

public record Quantity(int value) {

    public Quantity {
        if (value <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다. 입력값: " + value);
        }
    }

    public static Quantity of(int value) {
        return new Quantity(value);
    }
}
