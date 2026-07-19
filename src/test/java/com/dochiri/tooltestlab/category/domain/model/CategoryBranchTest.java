package com.dochiri.tooltestlab.category.domain.model;

import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryStatus;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryBranchTest {

    @Test
    @DisplayName("create 성공")
    void create() {
        // given
        var expectedName = "전자기기";

        // when
        var cat = Category.create(expectedName);

        // then
        assertThat(cat.id()).isNotNull();
        assertThat(cat.name().value()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("equals - 다른 타입 false")
    void equalsDifferentType() {
        // given
        var cat = Category.create("전자기기");
        var otherObject = new Object();

        // when
        var result = cat.equals(otherObject);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("equals - 다른 인스턴스 같은 ID")
    void equalsDifferentInstanceSameId() {
        // given
        var c1 = Category.create("전자기기");
        var c2 = Category.restore(c1.id(), new CategoryName("의류"), CategoryStatus.ACTIVE);

        // when
        var result = c1.equals(c2);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("equals - 자기 자신과 비교하면 true")
    void equalsSelf() {
        // given
        var cat = Category.create("전자기기");

        // when
        var result = cat.equals(cat);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("equals - 다른 ID를 가지면 false")
    void equalsDifferentId() {
        // given
        var c1 = Category.create("전자기기");
        var c2 = Category.create("의류");

        // when
        var result = c1.equals(c2);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("hashCode는 id 기반")
    void hashCodeBasedOnId() {
        // given
        var cat = Category.create("전자기기");

        // when
        var hash = cat.hashCode();

        // then
        assertThat(hash).isEqualTo(cat.id().hashCode());
    }
}
