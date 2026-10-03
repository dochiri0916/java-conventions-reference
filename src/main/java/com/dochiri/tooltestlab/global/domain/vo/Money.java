package com.dochiri.tooltestlab.global.domain.vo;

import com.dochiri.tooltestlab.global.domain.exception.MoneyOverflowException;
import com.dochiri.tooltestlab.global.domain.exception.NegativeMoneyException;
import com.dochiri.tooltestlab.global.domain.exception.NegativeMoneyMultiplierException;

import static java.util.Objects.requireNonNull;

public final class Money {

    public static final Money ZERO = new Money(0);

    private final long amount;

    private Money(final long amount) {
        if (amount < 0) {
            throw new NegativeMoneyException(amount);
        }
        this.amount = amount;
    }

    public Money add(final Money other) {
        requireNonNull(other, "더할 금액은 null일 수 없습니다.");

        try {
            return new Money(
                    Math.addExact(this.amount, other.amount)
            );
        } catch (final ArithmeticException exception) {
            throw new MoneyOverflowException();
        }
    }

    public Money multiply(final int quantity) {
        if (quantity < 0) {
            throw new NegativeMoneyMultiplierException(quantity);
        }

        try {
            return new Money(
                    Math.multiplyExact(this.amount, (long) quantity)
            );
        } catch (final ArithmeticException exception) {
            throw new MoneyOverflowException();
        }
    }

    public static Money of(final long amount) {
        return new Money(amount);
    }

    public long amount() {
        return amount;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final Money other)) {
            return false;
        }
        return amount == other.amount;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(amount);
    }

    @Override
    public String toString() {
        return "Money[amount=" + amount + "]";
    }

}
