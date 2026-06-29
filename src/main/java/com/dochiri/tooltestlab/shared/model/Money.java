package com.dochiri.tooltestlab.shared.model;

import java.util.Objects;

public record Money(long amount) {

    public Money {
        if (amount < 0) {
            throw new IllegalArgumentException("금액은 0 이상이어야 합니다. 입력값: " + amount);
        }
    }

    public static Money of(long amount) {
        return new Money(amount);
    }

    public static Money ZERO = new Money(0);

    public Money add(Money other) {
        Objects.requireNonNull(other, "더할 금액은 null일 수 없습니다.");
        return new Money(this.amount + other.amount);
    }

    public Money multiply(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 0 이상이어야 합니다. 입력값: " + quantity);
        }
        return new Money(this.amount * quantity);
    }
}
