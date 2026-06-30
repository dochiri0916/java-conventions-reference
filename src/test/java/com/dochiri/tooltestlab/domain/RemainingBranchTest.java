package com.dochiri.tooltestlab.domain;

import com.dochiri.tooltestlab.cart.application.port.in.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartCommand;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.cart.domain.model.CartId;
import com.dochiri.tooltestlab.cart.domain.model.CartItem;
import com.dochiri.tooltestlab.cart.domain.model.CartItems;
import com.dochiri.tooltestlab.member.application.port.in.LoginMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberCommand;
import com.dochiri.tooltestlab.order.domain.model.OrderLine;
import com.dochiri.tooltestlab.order.domain.model.OrderLines;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.review.application.port.in.WriteReviewCommand;
import com.dochiri.tooltestlab.shared.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RemainingBranchTest {

    // ── Commands ──

    @Test
    @DisplayName("AddToCartCommand - null 상품ID")
    void addToCartNullProduct() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new AddToCartCommand("m1", null, 1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("AddToCartCommand - 공백 상품ID")
    void addToCartBlankProduct() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new AddToCartCommand("m1", "  ", 1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RemoveFromCartCommand - null 상품ID")
    void removeFromCartNullProduct() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RemoveFromCartCommand("m1", null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RemoveFromCartCommand - 공백 상품ID")
    void removeFromCartBlankProduct() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RemoveFromCartCommand("m1", "  "))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RemoveFromCartCommand - 공백 회원ID")
    void removeFromCartBlankMember() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RemoveFromCartCommand("  ", "p1"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("LoginMemberCommand - null 로그인")
    void loginMemberNull() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new LoginMemberCommand(null, "pw"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("LoginMemberCommand - 공백 이메일")
    void loginMemberBlank() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new LoginMemberCommand("  ", "pw"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterMemberCommand - null 이름")
    void registerMemberNullName() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RegisterMemberCommand("e@e.com", "pw", null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - null 회원")
    void writeReviewNullMember() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new WriteReviewCommand(null, "p1", 5, "content"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - null 상품")
    void writeReviewNullProduct() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new WriteReviewCommand("m1", null, 5, "content"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - 평점 0")
    void writeReviewRatingZero() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new WriteReviewCommand("m1", "p1", 0, "content"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - 평점 6")
    void writeReviewRatingSix() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new WriteReviewCommand("m1", "p1", 6, "content"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - null 내용")
    void writeReviewNullContent() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new WriteReviewCommand("m1", "p1", 5, null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - 공백 회원ID")
    void writeReviewBlankMember() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new WriteReviewCommand("  ", "p1", 5, "content"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - 공백 상품ID")
    void writeReviewBlankProduct() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new WriteReviewCommand("m1", "  ", 5, "content"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - 공백 내용")
    void writeReviewBlankContent() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new WriteReviewCommand("m1", "p1", 5, "  "))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("LoginMemberCommand - null 비밀번호")
    void loginMemberNullPassword() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new LoginMemberCommand("e@e.com", null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("LoginMemberCommand - 공백 비밀번호")
    void loginMemberBlankPassword() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new LoginMemberCommand("e@e.com", "  "))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterMemberCommand - null 비밀번호")
    void registerMemberNullPassword() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RegisterMemberCommand("e@e.com", null, "name"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterMemberCommand - 공백 비밀번호")
    void registerMemberBlankPassword() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RegisterMemberCommand("e@e.com", "  ", "name"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterMemberCommand - 공백 이름")
    void registerMemberBlankName() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RegisterMemberCommand("e@e.com", "pw", "  "))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterMemberCommand - null 이메일")
    void registerMemberNullEmail() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RegisterMemberCommand(null, "pw", "name"))
                .isInstanceOf(RuntimeException.class);
    }

    // ── CartItems / CartItem ──

    @Test
    @DisplayName("CartItem - 수량 증가")
    void cartItemIncrease() {
            // given

            // when

            // then

        var item = new CartItem(new ProductId("p1"), new ProductName("상품"), new Quantity(1), Money.of(1000));
        var increased = item.increaseQuantity(2);
        assertThat(increased.quantity().value()).isEqualTo(3);
        assertThat(increased.totalPrice()).isEqualTo(Money.of(3000));
    }

    @Test
    @DisplayName("CartItem - 수량 증가 0 이하 예외")
    void cartItemIncreaseInvalid() {
            // given

            // when

            // then

        var item = new CartItem(new ProductId("p1"), new ProductName("상품"), new Quantity(1), Money.of(1000));
        assertThatThrownBy(() -> item.increaseQuantity(0))
                .isInstanceOf(InvalidCartItemException.class);
    }

    @Test
    @DisplayName("CartItems - 없는 상품 교체 예외")
    void cartItemsReplaceNotFound() {
            // given

            // when

            // then

        var items = CartItems.empty();
        var item = new CartItem(new ProductId("p1"), new ProductName("상품"), new Quantity(1), Money.of(1000));
        var newItem = new CartItem(new ProductId("p1"), new ProductName("상품"), new Quantity(2), Money.of(1000));
        assertThatThrownBy(() -> items.replace(item, newItem))
                .isInstanceOf(InvalidCartItemException.class);
    }

    @Test
    @DisplayName("CartItems - 상품ID로 찾기 및 포함 여부")
    void cartItemsFind() {
            // given

            // when

            // then

        var item = new CartItem(new ProductId("p1"), new ProductName("상품"), new Quantity(1), Money.of(1000));
        var items = CartItems.from(List.of(item));
        assertThat(items.containsProduct(new ProductId("p1"))).isTrue();
        assertThat(items.containsProduct(new ProductId("p2"))).isFalse();
        assertThat(items.findByProductId(new ProductId("p1"))).isPresent();
        assertThat(items.findByProductId(new ProductId("p2"))).isEmpty();
    }

    // ── OrderLine ──

    @Test
    @DisplayName("OrderLine 총 가격")
    void orderLineTotalPrice() {
            // given

            // when

            // then

        var line = new OrderLine(new ProductId("p1"), new ProductName("상품"), new Quantity(3), Money.of(1000));
        assertThat(line.totalPrice()).isEqualTo(Money.of(3000));
    }

    @Test
    @DisplayName("OrderLines 배열로부터 생성")
    void orderLinesFromArray() {
            // given

            // when

            // then

        var line = new OrderLine(new ProductId("p1"), new ProductName("상품"), new Quantity(1), Money.of(1000));
        var lines = OrderLines.fromArray(line);
        assertThat(lines.size()).isEqualTo(1);
    }

    // ── CartId ──

    @Test
    @DisplayName("CartId null 예외")
    void cartIdNull() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new CartId(null))
                .isInstanceOf(NullPointerException.class);
    }
}
