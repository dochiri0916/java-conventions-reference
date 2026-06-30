package com.dochiri.tooltestlab.category.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryBranchTest {

    @Test
    @DisplayName("create 성공")
    void create() {
            // given

            // when

            // then

        var cat = Category.create(new CategoryName("전자기기"));
        assertThat(cat.id()).isNotNull();
        assertThat(cat.name().value()).isEqualTo("전자기기");
    }

    @Test
    @DisplayName("equals - 다른 타입 false")
    void equalsDifferentType() {
            // given

            // when

            // then

        var cat = Category.create(new CategoryName("전자기기"));
        assertThat(cat.equals("string")).isFalse();
    }

    @Test
    @DisplayName("equals - 다른 인스턴스 같은 ID")
    void equalsDifferentInstanceSameId() {
            // given

            // when

            // then

        var c1 = Category.create(new CategoryName("전자기기"));
        var c2 = new Category(c1.id(), new CategoryName("의류"));
        assertThat(c1.equals(c2)).isTrue();
    }

    @Test
    @DisplayName("equals - 자기 자신과 비교하면 true")
    void equalsSelf() {
            // given
        var cat = Category.create(new CategoryName("전자기기"));

            // when
        var result = cat.equals(cat);

            // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("equals - 다른 ID를 가지면 false")
    void equalsDifferentId() {
            // given
        var c1 = Category.create(new CategoryName("전자기기"));
        var c2 = Category.create(new CategoryName("의류"));

            // when
        var result = c1.equals(c2);

            // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("hashCode는 id 기반")
    void hashCodeBasedOnId() {
            // given
        var cat = Category.create(new CategoryName("전자기기"));

            // when
        var hash = cat.hashCode();

            // then
        assertThat(hash).isEqualTo(cat.id().hashCode());
    }
}
