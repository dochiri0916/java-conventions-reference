package com.dochiri.tooltestlab.catalog.category.domain.exception;

public abstract sealed class CategoryDomainException extends RuntimeException
        permits CategoryIdRequiredException,
        InvalidCategoryIdException,
        CategoryNameRequiredException,
        InvalidCategoryNameException,
        CategoryStatusRequiredException {
    protected CategoryDomainException(final String message) {
        super(message);
    }

}
