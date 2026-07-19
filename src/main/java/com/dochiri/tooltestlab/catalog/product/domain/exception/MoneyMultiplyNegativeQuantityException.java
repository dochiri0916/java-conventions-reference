package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class MoneyMultiplyNegativeQuantityException extends DomainException {

    public MoneyMultiplyNegativeQuantityException(final int quantity) {
        super(
                ProductDomainErrorCode.MONEY_MULTIPLIER_NEGATIVE,
                "금액에 곱할 수량은 0개 이상이어야 합니다. quantity=%d"
                        .formatted(quantity),
                Map.of("quantity", quantity)
        );
    }

}
