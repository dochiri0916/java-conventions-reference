package com.dochiri.tooltestlab.catalog.category.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

public final class InvalidCategoryNameException extends DomainException {

    private InvalidCategoryNameException() {
        super(CategoryDomainErrorCode.CATEGORY_NAME_INVALID);
    }

    public static InvalidCategoryNameException invalid() {
        return new InvalidCategoryNameException();
    }

}
