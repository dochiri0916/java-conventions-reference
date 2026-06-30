package com.dochiri.tooltestlab.category.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidCategoryIdException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CategoryDomainErrorCode errorCode;

    private InvalidCategoryIdException(final CategoryDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidCategoryIdException invalidCatId() {
        return new InvalidCategoryIdException(CategoryDomainErrorCode.INVALID_CAT_ID);
    }

    public CategoryDomainErrorCode code() {
        return errorCode;
    }
}
