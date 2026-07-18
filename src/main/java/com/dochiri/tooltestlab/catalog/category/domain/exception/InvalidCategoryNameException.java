package com.dochiri.tooltestlab.catalog.category.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidCategoryNameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CategoryDomainErrorCode errorCode;

    private InvalidCategoryNameException(final CategoryDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidCategoryNameException invalidCatName() {
        return new InvalidCategoryNameException(CategoryDomainErrorCode.INVALID_CAT_NAME);
    }

    public static InvalidCategoryNameException required() {
        return new InvalidCategoryNameException(CategoryDomainErrorCode.CATEGORY_NAME_REQUIRED);
    }

    public CategoryDomainErrorCode code() {
        return errorCode;
    }
}
