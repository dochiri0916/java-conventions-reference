package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InvalidMoneyException;

public record Money(long amount) {

    public static final Money ZERO = new Money(0);

    public Money {
        if (amount < 0) {
            throw InvalidMoneyException.negative(amount);
        }
    }

    public static Money of(long amount) {
        return new Money(amount);
    }

    public Money add(Money other) {
        if (other == null) {
            throw InvalidMoneyException.addNull();
        }
        return new Money(this.amount + other.amount);
    }

    public Money multiply(int quantity) {
        if (quantity < 0) {
            throw InvalidMoneyException.multiplyNegative(quantity);
        }
        return new Money(this.amount * quantity);
    }
}
