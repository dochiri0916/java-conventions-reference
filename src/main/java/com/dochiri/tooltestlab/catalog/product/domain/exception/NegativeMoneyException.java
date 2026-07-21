package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class NegativeMoneyException extends RuntimeException {

    private final long amount;

    public NegativeMoneyException(final long amount) {
        super("금액은 0원 이상이어야 합니다.");
        this.amount = amount;
    }

    public long amount() {
        return amount;
    }

}
