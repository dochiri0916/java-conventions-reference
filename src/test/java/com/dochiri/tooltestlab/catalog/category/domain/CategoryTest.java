package com.dochiri.tooltestlab.catalog.category.domain;

import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    @DisplayName("카테고리 이름과 상태 변경은 새 객체를 반환하고 ID와 원본을 유지한다")
    void changesCategoryImmutably() {
        // given
        final Category original = Category.create(CategoryName.of("카테고리"));

        // when
        final Category renamed = original.rename(CategoryName.of("변경한 카테고리"));
        final Category inactive = renamed.deactivate();
        final Category deleted = inactive.delete();

        // then
        assertEquals("카테고리", original.name().value());
        assertEquals(CategoryStatus.ACTIVE, original.state());
        assertEquals("변경한 카테고리", renamed.name().value());
        assertEquals(CategoryStatus.INACTIVE, inactive.state());
        assertEquals(CategoryStatus.DELETED, deleted.state());
        assertEquals(original.id(), deleted.id());
        assertEquals(renamed.name(), deleted.name());
        assertNotSame(original, renamed);
    }

    @Test
    @DisplayName("카테고리 복원은 null 필수 필드를 거부한다")
    void rejectsNullFields() {
        // given
        final Category category = Category.create(CategoryName.of("카테고리"));

        // when
        // then
        assertThrows(NullPointerException.class, () -> Category.restore(null, category.name(), category.state()));
        assertThrows(NullPointerException.class, () -> Category.restore(category.id(), null, category.state()));
        assertThrows(NullPointerException.class, () -> Category.restore(category.id(), category.name(), null));
    }
}
