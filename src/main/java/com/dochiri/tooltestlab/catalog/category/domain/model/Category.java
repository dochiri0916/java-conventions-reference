package com.dochiri.tooltestlab.catalog.category.domain.model;

import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryIdRequiredException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryNameRequiredException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryStatusRequiredException;

public final class Category {

    private final CategoryId id;
    private final CategoryName name;
    private final CategoryStatus status;

    private Category(
            final CategoryId id,
            final CategoryName name,
            final CategoryStatus status
    ) {
        this.id = validated(id, new CategoryIdRequiredException());
        this.name = validated(name, new CategoryNameRequiredException());
        this.status = validated(status, new CategoryStatusRequiredException());
    }

    public static Category create(final String name) {
        return new Category(
                CategoryId.generate(),
                new CategoryName(name),
                CategoryStatus.ACTIVE
        );
    }

    public static Category restore(
            final CategoryId id,
            final CategoryName name,
            final CategoryStatus status
    ) {
        return new Category(id, name, status);
    }

    public Category rename(final String name) {
        return new Category(
                id,
                new CategoryName(name),
                status
        );
    }

    public Category deactivate() {
        return new Category(
                id,
                name,
                CategoryStatus.INACTIVE
        );
    }

    public Category delete() {
        return new Category(
                id,
                name,
                CategoryStatus.DELETED
        );
    }

    public CategoryId id() {
        return id;
    }

    public CategoryName name() {
        return name;
    }

    public CategoryStatus state() {
        return status;
    }

    private static <T> T validated(final T value, final RuntimeException exception) {
        if (value == null) {
            throw exception;
        }
        return value;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other
                || other instanceof Category category
                && id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
