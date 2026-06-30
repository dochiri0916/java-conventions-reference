package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InsufficientStockException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidStockException;

public record Stock(int value) {

    public Stock {
        if (value < 0) {
            throw InvalidStockException.invalidStock("재고는 0 이상이어야 합니다. 입력값: " + value);
        }
    }

    public static Stock from(final int value) {
        return new Stock(value);
    }

    public Stock decrease(final int quantity) {
        if (quantity <= 0) {
            throw InvalidStockException.invalidStock("감소 수량은 1 이상이어야 합니다. 입력값: " + quantity);
        }
        if (this.value < quantity) {
            throw InsufficientStockException.lowStock(this.value, quantity);
        }
        return new Stock(value - quantity);
    }

    public Stock increase(final int quantity) {
        if (quantity <= 0) {
            throw InvalidStockException.invalidStock("증가 수량은 1 이상이어야 합니다. 입력값: " + quantity);
        }
        return new Stock(value + quantity);
    }

    public boolean isZero() {
        return value == 0;
    }

    public boolean isPositive() {
        return value > 0;
    }
}
