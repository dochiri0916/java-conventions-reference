package com.dochiri.tooltestlab.domain;

import com.dochiri.tooltestlab.cart.application.port.in.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartCommand;
import com.dochiri.tooltestlab.catalog.category.application.port.in.command.RegisterCategoryCommand;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandValidationTest {

    @Test
    @DisplayName("RegisterCategoryCommand - null 이름")
    @SuppressFBWarnings(value = "NP", justification = "의도적인 null 입력 검증 테스트")
    void registerCategoryNullName() {
        // given
        String nullName = null;

        // when & then
        assertThatThrownBy(() -> new RegisterCategoryCommand(nullName))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterCategoryCommand - 공백 이름")
    void registerCategoryBlankName() {
        // given
        var blankName = "  ";

        // when & then
        assertThatThrownBy(() -> new RegisterCategoryCommand(blankName))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("AddToCartCommand - null 회원ID")
    void addToCartNullMember() {
        // given
        var productId = "p1";
        var quantity = 1;

        // when & then
        assertThatThrownBy(() -> new AddToCartCommand(null, productId, quantity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("AddToCartCommand - 공백 회원ID")
    void addToCartBlankMember() {
        // given
        var blankMemberId = "  ";
        var productId = "p1";
        var quantity = 1;

        // when & then
        assertThatThrownBy(() -> new AddToCartCommand(blankMemberId, productId, quantity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("AddToCartCommand - quantity 0 이하")
    void addToCartInvalidQuantity() {
        // given
        var memberId = "m1";
        var productId = "p1";
        var zeroQuantity = 0;
        var negativeQuantity = -1;

        // when & then
        assertThatThrownBy(() -> new AddToCartCommand(memberId, productId, zeroQuantity))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new AddToCartCommand(memberId, productId, negativeQuantity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RemoveFromCartCommand - null 회원ID")
    void removeFromCartNullMember() {
        // given
        var productId = "p1";

        // when & then
        assertThatThrownBy(() -> new RemoveFromCartCommand(null, productId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterProductCommand - null 이름")
    void registerProductNullName() {
        // given
        var description = "desc";
        var price = 1000L;
        var stock = 10;
        List<String> categoryIds = List.of();

        // when & then
        assertThatThrownBy(() -> new RegisterProductCommand(null, description, 1000, stock, categoryIds))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterProductCommand - 공백 이름")
    void registerProductBlankName() {
        // given
        var blankName = "  ";
        var description = "desc";
        var price = 1000L;
        var stock = 10;
        List<String> categoryIds = List.of();

        // when & then
        assertThatThrownBy(() -> new RegisterProductCommand(blankName, description, 1000, stock, categoryIds))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterProductCommand - 음수 가격")
    void registerProductNegativePrice() {
        // given
        var name = "name";
        var description = "desc";
        var negativePrice = -1L;
        var stock = 10;
        List<String> categoryIds = List.of();

        // when & then
        assertThatThrownBy(() -> new RegisterProductCommand(name, description, -1, stock, categoryIds))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterProductCommand - 음수 재고")
    void registerProductNegativeStock() {
        // given
        var name = "name";
        var description = "desc";
        var price = 1000L;
        var negativeStock = -1;
        List<String> categoryIds = List.of();

        // when & then
        assertThatThrownBy(() -> new RegisterProductCommand(name, description, 1000, negativeStock, categoryIds))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterProductCommand - null 카테고리ID는 빈 목록으로 변환")
    void registerProductNullCategoryIds() {
        // given
        var name = "name";
        var description = "desc";
        var price = 1000L;
        var stock = 10;

        // when
        var cmd = new RegisterProductCommand(name, description, 1000, stock, null);

        // then
        assertThat(cmd.categoryIds()).isEmpty();
    }
}
