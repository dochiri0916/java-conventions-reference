package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

public final class MoneyOverflowException extends DomainException {

    public MoneyOverflowException() {
        super(ProductDomainErrorCode.MONEY_OVERFLOW);
    }

}
