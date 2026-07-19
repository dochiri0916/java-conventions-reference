package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class NegativeMoneyException extends DomainException {

    public NegativeMoneyException(final long amount) {
        super(
                ProductDomainErrorCode.MONEY_NEGATIVE,
                "금액은 0원 이상이어야 합니다. amount=%d"
                        .formatted(amount),
                Map.of("amount", amount)
        );
    }

}
