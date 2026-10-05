package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryMapperTest {

    @Test
    @DisplayName("카테고리 UUID v7과 이름 및 상태를 저장 형식으로 변환하고 복원한다")
    void maps_and_updates_category() {
        // given
        final Category category = Category.create(CategoryName.of("카테고리"));

        // when
        final CategoryEntity entity = CategoryMapper.toEntity(category);
        final Category restored = CategoryMapper.toDomain(entity);
        final Category changed = category.rename(CategoryName.of("변경한 카테고리")).deactivate();
        entity.update(changed.name().value(), changed.state().name());
        final Category updated = CategoryMapper.toDomain(entity);

        // then
        assertEquals(7, restored.id().categoryId().version());
        assertEquals(36, entity.getPublicId().length());
        assertEquals(category.id(), restored.id());
        assertEquals(category.name(), restored.name());
        assertEquals(category.state(), restored.state());
        assertEquals(category.id(), updated.id());
        assertEquals(changed.name(), updated.name());
        assertEquals(changed.state(), updated.state());
        assertEquals(changed.state().name(), entity.getStatus());
        assertEquals("카테고리", category.name().value());
        assertTrue(category.state().isActive());
    }
}
