package com.dochiri.tooltestlab.product.domain.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.dochiri.tooltestlab.product.domain.exception.InsufficientStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressFBWarnings(value = "RV", justification = "Return value used to verify immutability or exception")
class ProductBranchTest {

    @Test
    @DisplayName("create - stock > 0이면 ON_SALE")
    void createOnSale() {
        // given
        var price = Money.of(1000);
        var stock = 10;

        // when
        var p = Product.create("상품", "설명", price, stock);

        // then
        assertThat(p.status()).isEqualTo(ProductStatus.ON_SALE);
        assertThat(p.stock().value()).isEqualTo(stock);
    }

    @Test
    @DisplayName("create - stock 0이면 SOLD_OUT")
    void createSoldOut() {
        // given
        var price = Money.of(1000);
        var stock = 0;

        // when
        var p = Product.create("상품", "설명", price, stock);

        // then
        assertThat(p.status()).isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("decreaseStock 성공")
    void decreaseStockSuccess() {
        // given
        var p = Product.create("상품", "설명", Money.of(1000), 10);
        var decreaseAmount = 3;

        // when
        var decreased = p.decreaseStock(decreaseAmount);

        // then
        assertThat(decreased.stock().value()).isEqualTo(7);
        assertThat(decreased.status()).isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("decreaseStock - 재고가 0이 되면 SOLD_OUT")
    void decreaseStockToZero() {
        // given
        var p = Product.create("상품", "설명", Money.of(1000), 3);
        var decreaseAmount = 3;

        // when
        var decreased = p.decreaseStock(decreaseAmount);

        // then
        assertThat(decreased.stock().value()).isEqualTo(0);
        assertThat(decreased.status()).isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("decreaseStock - 재고 부족 예외")
    void decreaseStockInsufficient() {
        // given
        var p = Product.create("상품", "설명", Money.of(1000), 3);

        // when & then
        assertThatThrownBy(() -> p.decreaseStock(5))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    @DisplayName("increaseStock 성공")
    void increaseStockSuccess() {
        // given
        var p = Product.create("상품", "설명", Money.of(1000), 3);
        var increaseAmount = 5;

        // when
        var increased = p.increaseStock(increaseAmount);

        // then
        assertThat(increased.stock().value()).isEqualTo(8);
        assertThat(increased.status()).isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("단종 처리")
    void discontinue() {
        // given
        var p = Product.create("상품", "설명", Money.of(1000), 10);

        // when
        var discontinued = p.discontinue();

        // then
        assertThat(discontinued.status()).isEqualTo(ProductStatus.DISCONTINUED);
    }

    @Test
    @DisplayName("equals - 다른 타입 false")
    void equalsDifferentType() {
        // given
        var p = Product.create("상품", "설명", Money.of(1000), 10);
        var otherObject = new Object();

        // when
        var result = p.equals(otherObject);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("equals - 같은 ID 다른 인스턴스")
    void equalsDifferentInstanceSameId() {
        // given
        var p1 = Product.create("상품", "설명", Money.of(1000), 10);
        var p2 = new Product(p1.id(), new ProductName("다른"), new ProductDescription("다른"),
                Money.of(2000), new Stock(5), ProductStatus.DISCONTINUED);

        // when
        var result = p1.equals(p2);

        // then
        assertThat(result).isTrue();
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
        var name = "상품";
        var price = Money.of(1000);
        var stock = 10;

        // when & then
        assertThatThrownBy(() -> Product.create(name, null, price, stock))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Stock.increase - 0 이하 예외")
    void stockIncreaseInvalid() {
        // given
        var stock = new Stock(10);

        // when & then
        assertThatThrownBy(() -> stock.increase(0))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Stock.from 팩토리")
    void stockFrom() {
        // given
        var expectedValue = 5;

        // when
        var s = Stock.from(expectedValue);

        // then
        assertThat(s.value()).isEqualTo(expectedValue);
    }
}
