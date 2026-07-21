package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class ProductDescriptionRequiredException extends RuntimeException {

    public ProductDescriptionRequiredException() {
        super("상품 설명은 필수입니다.");
    }

}
