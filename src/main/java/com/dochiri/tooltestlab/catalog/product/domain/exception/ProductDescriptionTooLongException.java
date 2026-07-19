package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class ProductDescriptionTooLongException extends DomainException {

    public ProductDescriptionTooLongException(final int maximumLength) {
        super(
                ProductDomainErrorCode.PRODUCT_DESCRIPTION_TOO_LONG,
                "상품 설명은 최대 %d자 이하이어야 합니다."
                        .formatted(maximumLength),
                Map.of("maximumLength", maximumLength)
        );
    }

}
