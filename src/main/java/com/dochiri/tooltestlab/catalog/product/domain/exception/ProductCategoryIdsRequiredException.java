package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class ProductCategoryIdsRequiredException extends RuntimeException {

    public ProductCategoryIdsRequiredException() {
        super("상품 카테고리 목록은 필수입니다.");
    }

}
