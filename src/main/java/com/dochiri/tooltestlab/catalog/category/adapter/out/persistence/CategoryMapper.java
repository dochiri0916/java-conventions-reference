package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;

final class CategoryMapper {

    private CategoryMapper() {
    }

    static CategoryEntity toEntity(final Category category) {
        return CategoryEntity.create(
                category.id().value(),
                category.name().value(),
                category.state()
        );
    }

    static Category toDomain(final CategoryEntity entity) {
        return Category.restore(
                new CategoryId(entity.getPublicId()),
                new CategoryName(entity.getName()),
                entity.getStatus()
        );
    }
}
