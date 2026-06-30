package com.dochiri.tooltestlab.domain;

import com.dochiri.tooltestlab.cart.application.port.in.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartCommand;
import com.dochiri.tooltestlab.category.application.port.in.RegisterCategoryCommand;
import com.dochiri.tooltestlab.product.application.port.in.RegisterProductCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandValidationTest {

    @Test
    @DisplayName("RegisterCategoryCommand - null 이름")
    void registerCategoryNullName() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RegisterCategoryCommand(null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterCategoryCommand - 공백 이름")
    void registerCategoryBlankName() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RegisterCategoryCommand("  "))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("AddToCartCommand - null 회원ID")
    void addToCartNullMember() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new AddToCartCommand(null, "p1", 1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("AddToCartCommand - 공백 회원ID")
    void addToCartBlankMember() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new AddToCartCommand("  ", "p1", 1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("AddToCartCommand - quantity 0 이하")
    void addToCartInvalidQuantity() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new AddToCartCommand("m1", "p1", 0))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new AddToCartCommand("m1", "p1", -1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RemoveFromCartCommand - null 회원ID")
    void removeFromCartNullMember() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RemoveFromCartCommand(null, "p1"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterProductCommand - null 이름")
    void registerProductNullName() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RegisterProductCommand(null, "desc", 1000, 10, List.of()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterProductCommand - 공백 이름")
    void registerProductBlankName() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RegisterProductCommand("  ", "desc", 1000, 10, List.of()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterProductCommand - 음수 가격")
    void registerProductNegativePrice() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RegisterProductCommand("name", "desc", -1, 10, List.of()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterProductCommand - 음수 재고")
    void registerProductNegativeStock() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RegisterProductCommand("name", "desc", 1000, -1, List.of()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterProductCommand - null 카테고리ID는 빈 목록으로 변환")
    void registerProductNullCategoryIds() {
            // given

            // when

            // then

        var cmd = new RegisterProductCommand("name", "desc", 1000, 10, null);
        assertThat(cmd.categoryIds()).isEmpty();
    }
}
