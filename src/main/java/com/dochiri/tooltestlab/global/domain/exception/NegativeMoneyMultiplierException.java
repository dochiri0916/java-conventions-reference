package com.dochiri.tooltestlab.global.domain.exception;

public final class NegativeMoneyMultiplierException extends GlobalDomainException {

    private final int quantity;

    public NegativeMoneyMultiplierException(final int quantity) {
        super("수량은 0개 이상이어야 합니다.");
        this.quantity = quantity;
    }

    public int quantity() {
        return quantity;
    }

}
