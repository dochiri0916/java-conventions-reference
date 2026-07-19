package com.dochiri.tooltestlab.domain;

import com.dochiri.tooltestlab.cart.application.port.in.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartCommand;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartIdException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.cart.domain.model.CartId;
import com.dochiri.tooltestlab.cart.domain.model.CartItem;
import com.dochiri.tooltestlab.cart.domain.model.CartItems;
import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.application.port.in.LoginMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberCommand;
import com.dochiri.tooltestlab.order.domain.model.OrderLine;
import com.dochiri.tooltestlab.order.domain.model.OrderLines;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.review.application.port.in.WriteReviewCommand;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressFBWarnings(value = "RV", justification = "Return value used to verify immutability or exception")
class RemainingBranchTest {

    // ── Commands ──

    @Test
    @DisplayName("AddToCartCommand - null 상품ID")
    void addToCartNullProduct() {
        // given
        var memberId = "m1";
        var quantity = 1;

        // when & then
        assertThatThrownBy(() -> new AddToCartCommand(memberId, null, quantity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("AddToCartCommand - 공백 상품ID")
    void addToCartBlankProduct() {
        // given
        var memberId = "m1";
        var blankProductId = "  ";
        var quantity = 1;

        // when & then
        assertThatThrownBy(() -> new AddToCartCommand(memberId, blankProductId, quantity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RemoveFromCartCommand - null 상품ID")
    void removeFromCartNullProduct() {
        // given
        var memberId = "m1";

        // when & then
        assertThatThrownBy(() -> new RemoveFromCartCommand(memberId, null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RemoveFromCartCommand - 공백 상품ID")
    void removeFromCartBlankProduct() {
        // given
        var memberId = "m1";
        var blankProductId = "  ";

        // when & then
        assertThatThrownBy(() -> new RemoveFromCartCommand(memberId, blankProductId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RemoveFromCartCommand - 공백 회원ID")
    void removeFromCartBlankMember() {
        // given
        var blankMemberId = "  ";
        var productId = "p1";

        // when & then
        assertThatThrownBy(() -> new RemoveFromCartCommand(blankMemberId, productId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("LoginMemberCommand - null 로그인")
    void loginMemberNull() {
        // given
        var password = "pw";

        // when & then
        assertThatThrownBy(() -> new LoginMemberCommand(null, password))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("LoginMemberCommand - 공백 이메일")
    void loginMemberBlank() {
        // given
        var blankEmail = "  ";
        var password = "pw";

        // when & then
        assertThatThrownBy(() -> new LoginMemberCommand(blankEmail, password))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterMemberCommand - null 이름")
    void registerMemberNullName() {
        // given
        var email = "e@e.com";
        var password = "pw";

        // when & then
        assertThatThrownBy(() -> new RegisterMemberCommand(email, password, null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - null 회원")
    void writeReviewNullMember() {
        // given
        var productId = "p1";
        var rating = 5;
        var content = "content";

        // when & then
        assertThatThrownBy(() -> new WriteReviewCommand(null, productId, rating, content))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - null 상품")
    void writeReviewNullProduct() {
        // given
        var memberId = "m1";
        var rating = 5;
        var content = "content";

        // when & then
        assertThatThrownBy(() -> new WriteReviewCommand(memberId, null, rating, content))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - 평점 0")
    void writeReviewRatingZero() {
        // given
        var memberId = "m1";
        var productId = "p1";
        var invalidRating = 0;
        var content = "content";

        // when & then
        assertThatThrownBy(() -> new WriteReviewCommand(memberId, productId, invalidRating, content))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - 평점 6")
    void writeReviewRatingSix() {
        // given
        var memberId = "m1";
        var productId = "p1";
        var invalidRating = 6;
        var content = "content";

        // when & then
        assertThatThrownBy(() -> new WriteReviewCommand(memberId, productId, invalidRating, content))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - null 내용")
    void writeReviewNullContent() {
        // given
        var memberId = "m1";
        var productId = "p1";
        var rating = 5;

        // when & then
        assertThatThrownBy(() -> new WriteReviewCommand(memberId, productId, rating, null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - 공백 회원ID")
    void writeReviewBlankMember() {
        // given
        var blankMemberId = "  ";
        var productId = "p1";
        var rating = 5;
        var content = "content";

        // when & then
        assertThatThrownBy(() -> new WriteReviewCommand(blankMemberId, productId, rating, content))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - 공백 상품ID")
    void writeReviewBlankProduct() {
        // given
        var memberId = "m1";
        var blankProductId = "  ";
        var rating = 5;
        var content = "content";

        // when & then
        assertThatThrownBy(() -> new WriteReviewCommand(memberId, blankProductId, rating, content))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("WriteReviewCommand - 공백 내용")
    void writeReviewBlankContent() {
        // given
        var memberId = "m1";
        var productId = "p1";
        var rating = 5;
        var blankContent = "  ";

        // when & then
        assertThatThrownBy(() -> new WriteReviewCommand(memberId, productId, rating, blankContent))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("LoginMemberCommand - null 비밀번호")
    void loginMemberNullPassword() {
        // given
        var email = "e@e.com";

        // when & then
        assertThatThrownBy(() -> new LoginMemberCommand(email, null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("LoginMemberCommand - 공백 비밀번호")
    void loginMemberBlankPassword() {
        // given
        var email = "e@e.com";
        var blankPassword = "  ";

        // when & then
        assertThatThrownBy(() -> new LoginMemberCommand(email, blankPassword))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterMemberCommand - null 비밀번호")
    void registerMemberNullPassword() {
        // given
        var email = "e@e.com";
        var name = "name";

        // when & then
        assertThatThrownBy(() -> new RegisterMemberCommand(email, null, name))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterMemberCommand - 공백 비밀번호")
    void registerMemberBlankPassword() {
        // given
        var email = "e@e.com";
        var blankPassword = "  ";
        var name = "name";

        // when & then
        assertThatThrownBy(() -> new RegisterMemberCommand(email, blankPassword, name))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterMemberCommand - 공백 이름")
    void registerMemberBlankName() {
        // given
        var email = "e@e.com";
        var password = "pw";
        var blankName = "  ";

        // when & then
        assertThatThrownBy(() -> new RegisterMemberCommand(email, password, blankName))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("RegisterMemberCommand - null 이메일")
    void registerMemberNullEmail() {
        // given
        var password = "pw";
        var name = "name";

        // when & then
        assertThatThrownBy(() -> new RegisterMemberCommand(null, password, name))
                .isInstanceOf(RuntimeException.class);
    }

    // ── CartItems / CartItem ──

    @Test
    @DisplayName("CartItem - 수량 증가")
    void cartItemIncrease() {
        // given
        var item = new CartItem(
                new ProductId("p1"),
                new ProductName("상품"),
                new Quantity(1),
                new Money(1000)
        );
        var increaseBy = 2;

        // when
        var increased = item.increaseQuantity(increaseBy);

        // then
        assertThat(increased.quantity().value()).isEqualTo(3);
        assertThat(increased.totalPrice()).isEqualTo(new Money(3000));
    }

    @Test
    @DisplayName("CartItem - 수량 증가 0 이하 예외")
    void cartItemIncreaseInvalid() {
        // given
        var item = new CartItem(
                new ProductId("p1"),
                new ProductName("상품"),
                new Quantity(1),
                new Money(1000)
        );
        var invalidIncrease = 0;

        // when & then
        assertThatThrownBy(() -> item.increaseQuantity(invalidIncrease))
                .isInstanceOf(InvalidCartItemException.class);
    }

    @Test
    @DisplayName("CartItems - 없는 상품 교체 예외")
    void cartItemsReplaceNotFound() {
        // given
        var items = CartItems.empty();
        var item = new CartItem(
                new ProductId("p1"),
                new ProductName("상품"),
                new Quantity(1),
                new Money(1000)
        );

        var newItem = new CartItem(
                new ProductId("p1"),
                new ProductName("상품"),
                new Quantity(2),
                new Money(1000)
        );

        // when & then
        assertThatThrownBy(() -> items.replace(item, newItem))
                .isInstanceOf(InvalidCartItemException.class);
    }

    @Test
    @DisplayName("CartItems - 상품ID로 찾기 및 포함 여부")
    void cartItemsFind() {
        // given
        var item = new CartItem(
                new ProductId("p1"),
                new ProductName("상품"),
                new Quantity(1),
                new Money(1000)
        );
        var items = CartItems.from(List.of(item));
        var existingProductId = new ProductId("p1");
        var nonExistingProductId = new ProductId("p2");

        // when
        var containsExisting = items.containsProduct(existingProductId);
        var containsNonExisting = items.containsProduct(nonExistingProductId);
        var found = items.findByProductId(existingProductId);
        var notFound = items.findByProductId(nonExistingProductId);

        // then
        assertThat(containsExisting).isTrue();
        assertThat(containsNonExisting).isFalse();
        assertThat(found).isPresent();
        assertThat(notFound).isEmpty();
    }

    // ── OrderLine ──

    @Test
    @DisplayName("OrderLine 총 가격")
    void orderLineTotalPrice() {
        // given
        var productId = new ProductId("p1");
        var productName = new ProductName("상품");
        var quantity = new Quantity(3);
        var unitPrice = new Money(1000);

        // when
        var line = new OrderLine(productId, productName, quantity, unitPrice);

        // then
        assertThat(line.totalPrice()).isEqualTo(new Money(3000));
    }

    @Test
    @DisplayName("OrderLines 배열로부터 생성")
    void orderLinesFromArray() {
        // given
        var line = new OrderLine(
                new ProductId("p1"),
                new ProductName("상품"),
                new Quantity(1),
                new Money(1000)
        );

        // when
        var lines = OrderLines.fromArray(line);

        // then
        assertThat(lines.size()).isEqualTo(1);
    }

    // ── CartId ──

    @Test
    @DisplayName("CartId null 예외")
    @SuppressFBWarnings(value = "NP", justification = "의도적인 null 입력 검증 테스트")
    void cartIdNull() {
        // given
        String nullValue = null;

        // when & then
        assertThatThrownBy(() -> new CartId(nullValue))
                .isInstanceOf(InvalidCartIdException.class);
    }
}
