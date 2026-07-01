package com.dochiri.tooltestlab.product.domain.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.dochiri.tooltestlab.product.domain.exception.InsufficientStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressFBWarnings(value = "RV", justification = "Return value used to verify immutability or exception")
class ProductTest {

    @Test
    @DisplayName("상품을 생성할 수 있다")
    void createProduct() {
        // given
        var expectedName = "노트북";
        var expectedPrice = 1_500_000;
        var expectedStock = 10;

        // when
        final Product product = Product.create(expectedName, "고성능 노트북", Money.of(expectedPrice), expectedStock);

        // then
        assertThat(product.id().value()).isNotBlank();
        assertThat(product.name().value()).isEqualTo(expectedName);
        assertThat(product.price().amount()).isEqualTo(expectedPrice);
        assertThat(product.stock().value()).isEqualTo(expectedStock);
        assertThat(product.status()).isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("재고가 0이면 SOLD_OUT 상태로 생성된다")
    void createWithZeroStock() {
        // given
        var price = Money.of(1000);
        var stock = 0;

        // when
        final Product product = Product.create("품절상품", "설명", price, stock);

        // then
        assertThat(product.status()).isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("재고를 감소시킬 수 있다")
    void decreaseStock() {
        // given
        final Product product = Product.create("노트북", "고성능 노트북", Money.of(1_500_000), 10);
        var decreaseAmount = 3;

        // when
        final Product decreased = product.decreaseStock(decreaseAmount);

        // then
        assertThat(decreased.stock().value()).isEqualTo(7);
        assertThat(decreased.status()).isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("재고가 정확히 0이 되면 SOLD_OUT 상태가 된다")
    void decreaseStockToZero() {
        // given
        final Product product = Product.create("노트북", "고성능 노트북", Money.of(1_500_000), 3);
        var decreaseAmount = 3;

        // when
        final Product decreased = product.decreaseStock(decreaseAmount);

        // then
        assertThat(decreased.stock().value()).isEqualTo(0);
        assertThat(decreased.status()).isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("재고보다 많이 감소시키면 예외가 발생한다")
    void decreaseStockInsufficient() {
        // given
        final Product product = Product.create("노트북", "고성능 노트북", Money.of(1_500_000), 3);

        // when & then
        assertThatThrownBy(() -> product.decreaseStock(5))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    @DisplayName("재고를 증가시킬 수 있다")
    void increaseStock() {
        // given
        final Product product = Product.create("노트북", "고성능 노트북", Money.of(1_500_000), 10);
        var increaseAmount = 5;

        // when
        final Product increased = product.increaseStock(increaseAmount);

        // then
        assertThat(increased.stock().value()).isEqualTo(15);
    }

    @Test
    @DisplayName("원본 Product는 불변이다")
    void immutability() {
        // given
        final Product product = Product.create("노트북", "고성능 노트북", Money.of(1_500_000), 10);
        var originalStock = product.stock().value();

        // when
        product.decreaseStock(3);

        // then
        assertThat(product.stock().value()).isEqualTo(originalStock);
    }
}
