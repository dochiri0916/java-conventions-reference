package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class InvalidCategoryIdsException extends ProductDomainException {

    public InvalidCategoryIdsException() {
        super("상품 카테고리 목록이 올바르지 않습니다.");
    }
}
