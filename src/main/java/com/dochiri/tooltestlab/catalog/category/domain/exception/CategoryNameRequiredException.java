package com.dochiri.tooltestlab.catalog.category.domain.exception;

public final class CategoryNameRequiredException extends CategoryDomainException {

    public CategoryNameRequiredException() {
        super("카테고리명은 필수입니다.");
    }

}
