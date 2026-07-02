package com.dochiri.tooltestlab.category.domain.model;

import com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryIdException;
import com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryNameException;

public record Category(
        CategoryId id,
        CategoryName name
) {

    public Category {
        if (id == null) {
            throw InvalidCategoryIdException.required();
        }
        if (name == null) {
            throw InvalidCategoryNameException.required();
        }
    }

    public static Category create(final CategoryName name) {
        return new Category(CategoryId.generate(), name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other
                || other instanceof Category category && id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
