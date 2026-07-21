package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class MoneyOverflowException extends RuntimeException {

    public MoneyOverflowException() {
        super("금액 계산 범위를 초과했습니다.");
    }

}
