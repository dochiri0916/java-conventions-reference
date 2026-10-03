package com.dochiri.tooltestlab.catalog.category.domain.exception;

public final class CategoryIdRequiredException extends CategoryDomainException {

    public CategoryIdRequiredException() {
        super("카테고리 ID는 필수입니다.");
    }

}
