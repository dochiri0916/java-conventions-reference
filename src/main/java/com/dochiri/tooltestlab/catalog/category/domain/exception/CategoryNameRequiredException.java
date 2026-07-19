package com.dochiri.tooltestlab.catalog.category.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

public final class CategoryNameRequiredException extends DomainException {

    private CategoryNameRequiredException() {
        super(CategoryDomainErrorCode.CATEGORY_NAME_REQUIRED);
    }

    public static CategoryNameRequiredException required() {
        return new CategoryNameRequiredException();
    }

}
