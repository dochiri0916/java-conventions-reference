package com.dochiri.tooltestlab.catalog.category.domain.exception;

public final class InvalidCategoryNameException extends CategoryDomainException {

    public InvalidCategoryNameException() {
        super("유효하지 않은 카테고리 이름입니다.");
    }

}
