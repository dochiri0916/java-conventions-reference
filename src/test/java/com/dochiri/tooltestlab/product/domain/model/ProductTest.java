package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InsufficientStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    @DisplayName("상품을 생성할 수 있다")
    void createProduct() {
        Product product = Product.create("노트북", "고성능 노트북", Money.of(1_500_000), 10);

        assertThat(product.id().value()).isNotBlank();
        assertThat(product.name().value()).isEqualTo("노트북");
        assertThat(product.price().amount()).isEqualTo(1_500_000);
        assertThat(product.stock().value()).isEqualTo(10);
        assertThat(product.status()).isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("재고가 0이면 SOLD_OUT 상태로 생성된다")
    void createWithZeroStock() {
        Product product = Product.create("품절상품", "설명", Money.of(1000), 0);
        assertThat(product.status()).isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("재고를 감소시킬 수 있다")
    void decreaseStock() {
        Product product = Product.create("노트북", "고성능 노트북", Money.of(1_500_000), 10);
        Product decreased = product.decreaseStock(3);
        assertThat(decreased.stock().value()).isEqualTo(7);
        assertThat(decreased.status()).isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("재고가 정확히 0이 되면 SOLD_OUT 상태가 된다")
    void decreaseStockToZero() {
        Product product = Product.create("노트북", "고성능 노트북", Money.of(1_500_000), 3);
        Product decreased = product.decreaseStock(3);
        assertThat(decreased.stock().value()).isEqualTo(0);
        assertThat(decreased.status()).isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("재고보다 많이 감소시키면 예외가 발생한다")
    void decreaseStockInsufficient() {
        Product product = Product.create("노트북", "고성능 노트북", Money.of(1_500_000), 3);
        assertThatThrownBy(() -> product.decreaseStock(5))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("재고가 부족합니다");
    }

    @Test
    @DisplayName("재고를 증가시킬 수 있다")
    void increaseStock() {
        Product product = Product.create("노트북", "고성능 노트북", Money.of(1_500_000), 10);
        Product increased = product.increaseStock(5);
        assertThat(increased.stock().value()).isEqualTo(15);
    }

    @Test
    @DisplayName("원본 Product는 불변이다")
    void immutability() {
        Product product = Product.create("노트북", "고성능 노트북", Money.of(1_500_000), 10);
        product.decreaseStock(3);
        assertThat(product.stock().value()).isEqualTo(10); // 원본 불변
    }
}
