package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class ProductDescriptionTooShortException extends ProductDomainException {

    private final int minLength;

    public ProductDescriptionTooShortException(final int minLength) {
        super("상품 설명은 최소 %d자 이상이어야 합니다."
                .formatted(minLength)
        );
        this.minLength = minLength;
    }

    public int minLength() {
        return minLength;
    }

}
