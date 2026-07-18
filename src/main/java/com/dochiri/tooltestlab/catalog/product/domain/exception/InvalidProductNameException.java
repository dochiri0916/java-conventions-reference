package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class InvalidProductNameException extends DomainException {

    public InvalidProductNameException(final String productName) {
        super(
                ProductDomainErrorCode.PRODUCT_ID_INVALID,
                "유효하지 않은 상품명입니다. productName='%s'"
                        .formatted(productName),
                Map.of("productName", productName)
        );
    }

}
