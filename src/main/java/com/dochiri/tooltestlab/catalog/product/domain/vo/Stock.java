package com.dochiri.tooltestlab.catalog.product.domain.vo;

import com.dochiri.tooltestlab.catalog.product.domain.exception.InsufficientStockException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.InvalidStockQuantityException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.StockOverflowException;
import com.dochiri.tooltestlab.global.domain.exception.NegativeMoneyException;

public final class Stock {

    private final int value;

    private Stock(final int value) {
        if (value < 0) {
            throw new NegativeMoneyException(value);
        }
        this.value = value;
    }

    public static Stock of(final int value) {
        return new Stock(value);
    }

    public int value() {
        return value;
    }

    public Stock decrease(final int quantity) {
        validatePositiveQuantity(quantity);

        if (value < quantity) {
            throw new InsufficientStockException(
                    value,
                    quantity
            );
        }

        return new Stock(value - quantity);
    }

    public Stock increase(final int quantity) {
        validatePositiveQuantity(quantity);

        final long increasedStock = (long) value + quantity;

        if (increasedStock > Integer.MAX_VALUE) {
            throw new StockOverflowException(
                    value,
                    quantity
            );
        }

        return new Stock((int) increasedStock);
    }

    public boolean hasStock() {
        return value > 0;
    }

    private static void validatePositiveQuantity(final int quantity) {
        if (quantity <= 0) {
            throw new InvalidStockQuantityException(quantity);
        }
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final Stock other)) {
            return false;
        }
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return "Stock[value=" + value + "]";
    }

}
