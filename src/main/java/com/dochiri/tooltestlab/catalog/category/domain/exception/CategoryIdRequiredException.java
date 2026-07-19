package com.dochiri.tooltestlab.catalog.category.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

public final class CategoryIdRequiredException extends DomainException {

    private CategoryIdRequiredException() {
        super(CategoryDomainErrorCode.CATEGORY_ID_REQUIRED);
    }

    public static CategoryIdRequiredException required() {
        return new CategoryIdRequiredException();
    }

}
