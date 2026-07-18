package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class InvalidProductIdException extends DomainException {

    public InvalidProductIdException(final String productId) {
        super(
                ProductDomainErrorCode.PRODUCT_ID_INVALID,
                "유효하지 않은 상품 ID입니다. productId='%s'"
                        .formatted(productId),
                Map.of("productId", productId)
        );
    }

}
