package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InsufficientStockException;

public record Stock(int value) {

    public Stock {
        if (value < 0) {
            throw new IllegalArgumentException("재고는 0 이상이어야 합니다. 입력값: " + value);
        }
    }

    public static Stock of(int value) {
        return new Stock(value);
    }

    public Stock decrease(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("감소 수량은 1 이상이어야 합니다. 입력값: " + quantity);
        }
        if (this.value < quantity) {
            throw new InsufficientStockException(this.value, quantity);
        }
        return new Stock(value - quantity);
    }

    public Stock increase(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("증가 수량은 1 이상이어야 합니다. 입력값: " + quantity);
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
