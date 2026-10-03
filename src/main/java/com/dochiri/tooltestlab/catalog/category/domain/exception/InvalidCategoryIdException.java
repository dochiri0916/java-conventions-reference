package com.dochiri.tooltestlab.catalog.category.domain.exception;

public final class InvalidCategoryIdException extends CategoryDomainException {

    public InvalidCategoryIdException() {
        super("유효하지 않은 카테고리 ID입니다.");
    }

}
