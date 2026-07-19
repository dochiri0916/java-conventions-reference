package com.dochiri.tooltestlab.catalog.category.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

public final class CategoryNameRequiredException extends DomainException {

    public CategoryNameRequiredException() {
        super(CategoryDomainErrorCode.CATEGORY_NAME_REQUIRED);
    }

}
