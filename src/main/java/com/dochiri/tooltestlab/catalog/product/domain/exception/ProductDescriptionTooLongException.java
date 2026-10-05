package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class ProductDescriptionTooLongException extends ProductDomainException {

    private final int maxLength;

    public ProductDescriptionTooLongException(final int maxLength) {
        super("상품 설명은 최대 %d자 이하여야 합니다."
                .formatted(maxLength)
        );
        this.maxLength = maxLength;
    }

    public int maxLength() {
        return maxLength;
    }

}
