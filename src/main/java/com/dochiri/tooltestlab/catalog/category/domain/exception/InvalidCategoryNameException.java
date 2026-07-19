package com.dochiri.tooltestlab.catalog.category.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

public final class InvalidCategoryNameException extends DomainException {

    public InvalidCategoryNameException() {
        super(CategoryDomainErrorCode.CATEGORY_NAME_INVALID);
    }

}
