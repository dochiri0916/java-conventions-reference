package com.dochiri.tooltestlab.category.domain.model;

import static java.util.Objects.requireNonNull;

public record Category(
        CategoryId id,
        CategoryName name
) {

    public Category {
        requireNonNull(id, "카테고리 ID는 null일 수 없습니다.");
        requireNonNull(name, "카테고리명은 null일 수 없습니다.");
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
