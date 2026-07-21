package com.dochiri.tooltestlab.catalog.product.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.exception.*;

public record Stock(int value) {

    public Stock {
        if (value < 0) {
            throw new NegativeMoneyException(value);
        }
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

}
