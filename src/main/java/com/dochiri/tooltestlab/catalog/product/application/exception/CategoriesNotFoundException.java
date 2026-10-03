package com.dochiri.tooltestlab.catalog.product.application.exception;

public final class CategoriesNotFoundException extends ProductApplicationException {

    public CategoriesNotFoundException() {
        super("일부 카테고리를 찾을 수 없습니다.");
    }

}
