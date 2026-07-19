package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class ProductDescriptionTooShortException extends DomainException {

    public ProductDescriptionTooShortException(final int minimumLength) {
        super(
                ProductDomainErrorCode.PRODUCT_DESCRIPTION_TOO_SHORT,
                "상품 설명은 최소 %d자 이상이어야 합니다."
                        .formatted(minimumLength),
                Map.of("minimumLength", minimumLength)
        );
    }

}
