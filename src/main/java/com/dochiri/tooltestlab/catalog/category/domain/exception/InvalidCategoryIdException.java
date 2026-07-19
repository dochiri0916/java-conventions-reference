package com.dochiri.tooltestlab.catalog.category.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

public final class InvalidCategoryIdException extends DomainException {

    private InvalidCategoryIdException() {
        super(CategoryDomainErrorCode.CATEGORY_ID_INVALID);
    }

    public static InvalidCategoryIdException invalid() {
        return new InvalidCategoryIdException();
    }

}
