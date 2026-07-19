package com.dochiri.tooltestlab.catalog.category.domain.model;

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
        this.id = requireNonNull(id, "카테고리 ID는 null일 수 없습니다.");
        this.name = requireNonNull(name, "카테고리명은 null일 수 없습니다.");
        this.status = requireNonNull(status, "카테고리 상태는 null일 수 없습니다.");
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
