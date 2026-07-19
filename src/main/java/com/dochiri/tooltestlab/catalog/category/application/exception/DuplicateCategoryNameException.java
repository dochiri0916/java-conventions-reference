package com.dochiri.tooltestlab.catalog.category.application.exception;

import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;

import com.dochiri.tooltestlab.global.exception.ApplicationException;

import java.util.Map;

public final class DuplicateCategoryNameException extends ApplicationException {

    private DuplicateCategoryNameException(final CategoryName categoryName) {
        super(
                CategoryErrorCode.DUPLICATE_CATEGORY_NAME,
                CategoryErrorCode.DUPLICATE_CATEGORY_NAME.detail(),
                Map.of("categoryName", categoryName.value())
        );
    }

    public static DuplicateCategoryNameException duplicateCategoryName(final CategoryName categoryName) {
        return new DuplicateCategoryNameException(categoryName);
    }
}
