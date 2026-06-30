package com.dochiri.tooltestlab.domain;

import com.dochiri.tooltestlab.cart.domain.model.CartId;
import com.dochiri.tooltestlab.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.category.domain.model.CategoryName;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.*;
import com.dochiri.tooltestlab.product.domain.model.ProductDescription;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.product.domain.model.Stock;
import com.dochiri.tooltestlab.review.domain.model.Rating;
import com.dochiri.tooltestlab.review.domain.model.ReviewContent;
import com.dochiri.tooltestlab.review.domain.model.ReviewId;
import com.dochiri.tooltestlab.shared.model.MaxCount;
import com.dochiri.tooltestlab.shared.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VoEdgeCaseTest {

    // ── MemberId ──
    @Test
    @DisplayName("MemberId blank 예외")
    void memberIdBlank() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new MemberId("  "))
                .isInstanceOf(RuntimeException.class);
    }

    // ── ProductId ──
    @Test
    @DisplayName("ProductId 정상 생성 및 generate")
    void productId() {
            // given

            // when

            // then

        var id = new ProductId("prod1");
        assertThat(id.value()).isEqualTo("prod1");
        var gen = ProductId.generate();
        assertThat(gen.value()).hasSize(32);
    }

    @Test
    @DisplayName("ProductId blank 예외")
    void productIdBlank() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new ProductId("  "))
                .isInstanceOf(RuntimeException.class);
    }

    // ── ProductName ──
    @Test
    @DisplayName("ProductName blank 예외")
    void productNameBlank() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new ProductName("  "))
                .isInstanceOf(RuntimeException.class);
    }

    // ── ProductDescription ──
    @Test
    @DisplayName("ProductDescription blank 예외")
    void productDescriptionBlank() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new ProductDescription("  "))
                .isInstanceOf(RuntimeException.class);
    }

    // ── Stock ──
    @Test
    @DisplayName("Stock 음수 예외")
    void stockNegative() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new Stock(-1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Stock 감소 예외")
    void stockDecreaseNegative() {
            // given

            // when

            // then

        var stock = new Stock(10);
        assertThatThrownBy(() -> stock.decrease(0))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> stock.decrease(11))
                .isInstanceOf(RuntimeException.class);
    }

    // ── Quantity ──
    @Test
    @DisplayName("Quantity 0 이하 예외")
    void quantityZeroOrLess() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new Quantity(0))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new Quantity(-1))
                .isInstanceOf(RuntimeException.class);
    }

    // ── OrderId ──
    @Test
    @DisplayName("OrderId blank 예외 및 generate")
    void orderId() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new OrderId("  "))
                .isInstanceOf(RuntimeException.class);
        var gen = OrderId.generate();
        assertThat(gen.value()).hasSize(32);
    }

    // ── CartId ──
    @Test
    @DisplayName("CartId blank 예외 및 generate")
    void cartId() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new CartId("  "))
                .isInstanceOf(RuntimeException.class);
        var gen = CartId.generate();
        assertThat(gen.value()).hasSize(32);
    }

    // ── CategoryId ──
    @Test
    @DisplayName("CategoryId blank 예외 및 generate")
    void categoryId() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new CategoryId("  "))
                .isInstanceOf(RuntimeException.class);
        var gen = CategoryId.generate();
        assertThat(gen.value()).hasSize(32);
    }

    // ── CategoryName ──
    @Test
    @DisplayName("CategoryName blank 예외")
    void categoryNameBlank() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new CategoryName("  "))
                .isInstanceOf(RuntimeException.class);
    }

    // ── Rating ──
    @Test
    @DisplayName("Rating 경계값")
    void ratingBoundary() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new Rating(0))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new Rating(6))
                .isInstanceOf(RuntimeException.class);
        assertThat(new Rating(1).value()).isEqualTo(1);
        assertThat(new Rating(5).value()).isEqualTo(5);
    }

    // ── ReviewContent ──
    @Test
    @DisplayName("ReviewContent blank 예외")
    void reviewContentBlank() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new ReviewContent("  "))
                .isInstanceOf(RuntimeException.class);
    }

    // ── ReviewId ──
    @Test
    @DisplayName("ReviewId blank 예외 및 generate")
    void reviewId() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new ReviewId("  "))
                .isInstanceOf(RuntimeException.class);
        var gen = ReviewId.generate();
        assertThat(gen.value()).hasSize(32);
    }

    // ── Shipping VOs ──
    @Test
    @DisplayName("Shipping VOs blank 예외")
    void shippingVosBlank() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new RecipientName("  "))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new Phone("  "))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new StreetAddress("  "))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new ZipCode("  "))
                .isInstanceOf(RuntimeException.class);
    }

    // ── MaxCount ──
    @Test
    @DisplayName("MaxCount 0 이하 예외")
    void maxCountInvalid() {
            // given

            // when

            // then

        assertThatThrownBy(() -> MaxCount.of(0))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> MaxCount.of(-1))
                .isInstanceOf(RuntimeException.class);
    }

    // ── Money ──
    @Test
    @DisplayName("Money 연산")
    void moneyOperations() {
            // given

            // when

            // then

        assertThat(Money.ZERO.amount()).isEqualTo(0);
        assertThat(Money.of(100).add(Money.of(200))).isEqualTo(Money.of(300));
        assertThat(Money.of(100).multiply(3)).isEqualTo(Money.of(300));
        assertThatThrownBy(() -> Money.of(-1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Quantity.from 정상")
    void quantityFrom() {
            // given

            // when

            // then

        var q = Quantity.from(5);
        assertThat(q.value()).isEqualTo(5);
    }

    @Test
    @DisplayName("Stock.increase 정상")
    void stockIncrease() {
            // given

            // when

            // then

        var stock = new Stock(5);
        var increased = stock.increase(3);
        assertThat(increased.value()).isEqualTo(8);
    }

    @Test
    @DisplayName("Stock - 0 확인 및 양수 확인")
    void stockMethods() {
            // given

            // when

            // then

        assertThat(new Stock(0).isZero()).isTrue();
        assertThat(new Stock(1).isPositive()).isTrue();
        assertThat(new Stock(0).isPositive()).isFalse();
    }

    @Test
    @DisplayName("OrderLine 총 가격")
    void orderLineTotalPrice2() {
            // given

            // when

            // then

        var line = new com.dochiri.tooltestlab.order.domain.model.OrderLine(
                new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("상품"),
                new Quantity(3), Money.of(1000));
        assertThat(line.totalPrice()).isEqualTo(Money.of(3000));
    }

    @Test
    @DisplayName("OrderLines 팩토리 생성")
    void orderLinesFromFactory() {
            // given

            // when

            // then

        var line = new com.dochiri.tooltestlab.order.domain.model.OrderLine(
                new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("상품"),
                new Quantity(1), Money.of(1000));
        var lines = com.dochiri.tooltestlab.order.domain.model.OrderLines.from(java.util.List.of(line));
        assertThat(lines.size()).isEqualTo(1);
    }
}
