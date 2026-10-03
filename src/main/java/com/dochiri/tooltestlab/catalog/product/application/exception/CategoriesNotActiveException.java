package com.dochiri.tooltestlab.catalog.product.application.exception;

public final class CategoriesNotActiveException extends ProductApplicationException {

    public CategoriesNotActiveException() {
        super("활성화되지 않은 카테고리가 포함되어 있습니다.");
    }

}
