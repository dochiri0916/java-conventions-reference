package com.dochiri.tooltestlab.catalog.product.domain.vo;

import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.product.domain.exception.InvalidCategoryIdsException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductCategoryIdsRequiredException;

import java.util.Objects;
import java.util.Set;

public final class CategoryIds {

    private final Set<CategoryId> values;

    private CategoryIds(final Set<CategoryId> values) {
        if (values == null) {
            throw new ProductCategoryIdsRequiredException();
        }
        if (values.stream().anyMatch(Objects::isNull)) {
            throw new InvalidCategoryIdsException();
        }
        this.values = Set.copyOf(values);
    }

    public static CategoryIds of(final Set<CategoryId> values) {
        return new CategoryIds(values);
    }

    public static CategoryIds empty() {
        return new CategoryIds(Set.of());
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public Set<CategoryId> values() {
        return values;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof CategoryIds categoryIds && values.equals(categoryIds.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public String toString() {
        return "CategoryIds[values=" + values + "]";
    }
}
