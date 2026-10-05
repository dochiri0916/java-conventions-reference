package com.dochiri.tooltestlab.catalog.category.domain;

import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryStatus;

import static java.util.Objects.requireNonNull;

public final class Category {

    private final CategoryId id;
    private final CategoryName name;
    private final CategoryStatus status;

    private Category(
            final CategoryId id,
            final CategoryName name,
            final CategoryStatus status
    ) {
        this.id = requireNonNull(id);
        this.name = requireNonNull(name);
        this.status = requireNonNull(status);
    }

    public static Category create(final CategoryName name) {
        return new Category(
                CategoryId.generate(),
                name,
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

    public Category rename(final CategoryName name) {
        return new Category(
                id,
                name,
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
