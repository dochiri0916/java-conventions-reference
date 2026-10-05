package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryStatus;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName;

final class CategoryMapper {

    private CategoryMapper() {
    }

    static CategoryEntity toEntity(final Category category) {
        return CategoryEntity.create(
                category.id().value(),
                category.name().value(),
                category.state().name()
        );
    }

    static Category toDomain(final CategoryEntity entity) {
        return Category.restore(
                CategoryId.parse(entity.getPublicId()),
                CategoryName.of(entity.getName()),
                CategoryStatus.valueOf(entity.getStatus())
        );
    }
}
