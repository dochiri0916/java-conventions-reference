package com.dochiri.tooltestlab.global.domain.exception;

public final class MoneyRequiredException extends GlobalDomainException {

    public MoneyRequiredException() {
        super("금액은 필수입니다.");
    }

}
