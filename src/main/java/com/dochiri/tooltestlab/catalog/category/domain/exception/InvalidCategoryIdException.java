package com.dochiri.tooltestlab.catalog.category.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

public final class InvalidCategoryIdException extends DomainException {

    public InvalidCategoryIdException() {
        super(CategoryDomainErrorCode.CATEGORY_ID_INVALID);
    }

}
