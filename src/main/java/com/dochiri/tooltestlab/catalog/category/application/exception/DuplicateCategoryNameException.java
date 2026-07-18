package com.dochiri.tooltestlab.catalog.category.application.exception;

import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;

import static java.util.Objects.requireNonNull;

public final class DuplicateCategoryNameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CategoryErrorCode errorCode;
    private final transient CategoryName duplicatedCategoryName;

    private DuplicateCategoryNameException(
            final CategoryErrorCode errorCode,
            final CategoryName categoryName) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.duplicatedCategoryName = requireNonNull(categoryName);
    }

    public static DuplicateCategoryNameException duplicateCategoryName(final CategoryName categoryName) {
        return new DuplicateCategoryNameException(CategoryErrorCode.DUPLICATE_CATEGORY_NAME, categoryName);
    }

    public CategoryErrorCode code() {
        return errorCode;
    }

    public CategoryName categoryName() {
        return duplicatedCategoryName;
    }
}
