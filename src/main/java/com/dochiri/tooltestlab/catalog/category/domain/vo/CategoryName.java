package com.dochiri.tooltestlab.catalog.category.domain.vo;

import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryNameRequiredException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.InvalidCategoryNameException;

public final class CategoryName {

    private final String value;

    private CategoryName(final String value) {
        if (value == null) {
            throw new CategoryNameRequiredException();
        }
        final String normalized = value.strip();
        if (normalized.isBlank()) {
            throw new InvalidCategoryNameException();
        }
        this.value = normalized;
    }

    public static CategoryName of(final String value) {
        return new CategoryName(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final CategoryName other)) {
            return false;
        }
        return value.equals(
                other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "CategoryName[value=" + value + "]";
    }

}
