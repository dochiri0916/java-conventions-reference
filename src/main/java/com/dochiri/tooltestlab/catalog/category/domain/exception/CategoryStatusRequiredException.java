package com.dochiri.tooltestlab.catalog.category.domain.exception;

public final class CategoryStatusRequiredException extends CategoryDomainException {

    public CategoryStatusRequiredException() {
        super("카테고리 상태는 필수입니다.");
    }

}
