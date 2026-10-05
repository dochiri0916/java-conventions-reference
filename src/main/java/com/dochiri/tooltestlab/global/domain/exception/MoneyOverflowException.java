package com.dochiri.tooltestlab.global.domain.exception;

public final class MoneyOverflowException extends GlobalDomainException {

    public MoneyOverflowException() {
        super("금액 계산 범위를 초과했습니다.");
    }

}
