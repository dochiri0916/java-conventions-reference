package com.dochiri.tooltestlab.catalog.product.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.exception.MoneyMultiplyNegativeQuantityException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.MoneyOverflowException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.NegativeMoneyException;

import static java.util.Objects.requireNonNull;

public record Money(long amount) {

    public static final Money ZERO = new Money(0);

    public Money {
        if (amount < 0) {
            throw new NegativeMoneyException(amount);
        }
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
            throw new MoneyMultiplyNegativeQuantityException(quantity);
        }

        try {
            return new Money(
                    Math.multiplyExact(this.amount, (long) quantity)
            );
        } catch (final ArithmeticException exception) {
            throw new MoneyOverflowException();
        }
    }
}
