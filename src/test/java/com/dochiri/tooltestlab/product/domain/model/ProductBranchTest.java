package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InsufficientStockException;
import com.dochiri.tooltestlab.shared.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductBranchTest {

    @Test
    @DisplayName("create - stock > 0이면 ON_SALE")
    void createOnSale() {
            // given

            // when

            // then

        var p = Product.create("상품", "설명", Money.of(1000), 10);
        assertThat(p.status()).isEqualTo(ProductStatus.ON_SALE);
        assertThat(p.stock().value()).isEqualTo(10);
    }

    @Test
    @DisplayName("create - stock 0이면 SOLD_OUT")
    void createSoldOut() {
            // given

            // when

            // then

        var p = Product.create("상품", "설명", Money.of(1000), 0);
        assertThat(p.status()).isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("decreaseStock 성공")
    void decreaseStockSuccess() {
            // given

            // when

            // then

        var p = Product.create("상품", "설명", Money.of(1000), 10);
        var decreased = p.decreaseStock(3);
        assertThat(decreased.stock().value()).isEqualTo(7);
        assertThat(decreased.status()).isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("decreaseStock - 재고가 0이 되면 SOLD_OUT")
    void decreaseStockToZero() {
            // given

            // when

            // then

        var p = Product.create("상품", "설명", Money.of(1000), 3);
        var decreased = p.decreaseStock(3);
        assertThat(decreased.stock().value()).isEqualTo(0);
        assertThat(decreased.status()).isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("decreaseStock - 재고 부족 예외")
    void decreaseStockInsufficient() {
            // given

            // when

            // then

        var p = Product.create("상품", "설명", Money.of(1000), 3);
        assertThatThrownBy(() -> p.decreaseStock(5))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    @DisplayName("increaseStock 성공")
    void increaseStockSuccess() {
            // given

            // when

            // then

        var p = Product.create("상품", "설명", Money.of(1000), 3);
        var increased = p.increaseStock(5);
        assertThat(increased.stock().value()).isEqualTo(8);
        assertThat(increased.status()).isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("단종 처리")
    void discontinue() {
            // given

            // when

            // then

        var p = Product.create("상품", "설명", Money.of(1000), 10);
        var discontinued = p.discontinue();
        assertThat(discontinued.status()).isEqualTo(ProductStatus.DISCONTINUED);
    }

    @Test
    @DisplayName("equals - 다른 타입 false")
    void equalsDifferentType() {
            // given

            // when

            // then

        var p = Product.create("상품", "설명", Money.of(1000), 10);
        assertThat(p.equals("string")).isFalse();
    }

    @Test
    @DisplayName("equals - 같은 ID 다른 인스턴스")
    void equalsDifferentInstanceSameId() {
            // given

            // when

            // then

        var p1 = Product.create("상품", "설명", Money.of(1000), 10);
        var p2 = new Product(p1.id(), new ProductName("다른"), new ProductDescription("다른"),
                Money.of(2000), new Stock(5), ProductStatus.DISCONTINUED);
        assertThat(p1.equals(p2)).isTrue();
    }

    @Test
    @DisplayName("equals - 자기 자신과 비교하면 true")
    void equalsSelf() {
            // given
        var p = Product.create("상품", "설명", Money.of(1000), 10);

            // when
        var result = p.equals(p);

            // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("equals - 다른 ID를 가지면 false")
    void equalsDifferentId() {
            // given
        var p1 = Product.create("상품A", "설명A", Money.of(1000), 10);
        var p2 = Product.create("상품B", "설명B", Money.of(2000), 20);

            // when
        var result = p1.equals(p2);

            // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("create - description이 null이면 예외 발생")
    void createWithNullDescription() {
            // given

            // when

            // then
        assertThatThrownBy(() -> Product.create("상품", null, Money.of(1000), 10))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Stock.increase - 0 이하 예외")
    void stockIncreaseInvalid() {
            // given

            // when

            // then

        var stock = new Stock(10);
        assertThatThrownBy(() -> stock.increase(0))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Stock.from 팩토리")
    void stockFrom() {
            // given

            // when

            // then

        var s = Stock.from(5);
        assertThat(s.value()).isEqualTo(5);
    }
}
