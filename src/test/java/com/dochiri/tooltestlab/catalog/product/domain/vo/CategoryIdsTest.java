package com.dochiri.tooltestlab.catalog.product.domain.vo;

import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.product.domain.exception.InvalidCategoryIdsException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductCategoryIdsRequiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryIdsTest {

    @Test
    @DisplayName("카테고리 ID 목록은 입력과 반환 컬렉션의 외부 변경을 차단한다")
    void protects_categories_from_external_mutation() {
        // given
        final CategoryId categoryId = CategoryId.generate();
        final Set<CategoryId> source = new HashSet<>(Set.of(categoryId));
        final CategoryIds categoryIds = CategoryIds.of(source);
        source.clear();

        // when & then
        assertEquals(Set.of(categoryId), categoryIds.values());
        assertEquals(CategoryIds.of(Set.of(categoryId)), categoryIds);
        assertEquals(CategoryIds.of(Set.of(categoryId)).hashCode(), categoryIds.hashCode());
        assertThrows(UnsupportedOperationException.class, () -> categoryIds.values().clear());
    }

    @Test
    @DisplayName("카테고리 미지정은 허용하지만 null 목록과 null 요소는 거부한다")
    void rejects_missing_collections_and_entries() {
        // given
        final Set<CategoryId> source = new HashSet<>();
        source.add(null);

        // when & then
        assertTrue(CategoryIds.empty().isEmpty());
        assertEquals(CategoryIds.empty(), CategoryIds.of(Set.of()));
        assertThrows(ProductCategoryIdsRequiredException.class, () -> CategoryIds.of(null));
        assertThrows(InvalidCategoryIdsException.class, () -> CategoryIds.of(source));
    }
}
