package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductStatus;
import com.dochiri.tooltestlab.catalog.product.domain.model.StockStatus;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDomainException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
        var expectedDescription = "고성능 노트북";
        var expectedPrice = 1_500_000;
        var expectedStock = 10;

        // when
        final Product product = Product.create(
                expectedName,
                expectedDescription,
                expectedPrice,
                expectedStock
        );

        // then
        assertThat(product.id().value()).isNotBlank();
        assertThat(product.name().value()).isEqualTo(expectedName);
        assertThat(product.description().value()).isEqualTo(expectedDescription);
        assertThat(product.price().amount()).isEqualTo(expectedPrice);
        assertThat(product.stock().value()).isEqualTo(expectedStock);
        assertThat(product.status()).isEqualTo(ProductStatus.PREPARING);
        assertThat(product.stockStatus()).isEqualTo(StockStatus.IN_STOCK);
    }

    @Test
    @DisplayName("재고가 0이면 품절 재고 상태로 생성된다")
    void createWithZeroStock() {
        // given & when
        final Product product = ProductFixture.productWithStock(0);

        // then
        assertThat(product.status()).isEqualTo(ProductStatus.PREPARING);
        assertThat(product.stockStatus()).isEqualTo(StockStatus.SOLD_OUT);
        assertThat(product.isPurchasable()).isFalse();
    }

    @Test
    @DisplayName("재고가 있으면 재고 있음 상태로 생성된다")
    void createWithStock() {
        // given & when
        final Product product = ProductFixture.productWithStock(10);

        // then
        assertThat(product.status()).isEqualTo(ProductStatus.PREPARING);
        assertThat(product.stockStatus()).isEqualTo(StockStatus.IN_STOCK);
    }

    @Test
    @DisplayName("재고를 감소시킬 수 있다")
    void decreaseStock() {
        // given
        final Product product = ProductFixture.productWithStock(10);
        int decreaseAmount = 3;

        // when
        final Product decreased = product.decreaseStock(decreaseAmount);

        // then
        assertThat(decreased.status()).isEqualTo(ProductStatus.PREPARING);
        assertThat(product.stockStatus()).isEqualTo(StockStatus.IN_STOCK);
        assertThat(decreased.stock().value()).isEqualTo(7);
    }

    @Test
    @DisplayName("재고가 정확히 0이 되면 SOLD_OUT 상태가 된다")
    void decreaseStockToZero() {
        // given
        final Product product = ProductFixture.productWithStock(3);
        int decreaseAmount = 3;

        // when
        final Product decreased = product.decreaseStock(decreaseAmount);

        // then
        assertThat(decreased.stock().value()).isEqualTo(0);
        assertThat(decreased.stockStatus()).isEqualTo(StockStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("재고보다 많이 감소시키면 예외가 발생한다")
    void decreaseStockInsufficient() {
        // given
        final Product product = ProductFixture.productWithStock(3);

        // when & then
        assertThatThrownBy(() -> product.decreaseStock(5))
                .isInstanceOf(ProductDomainException.class);
    }

    @Test
    @DisplayName("재고를 증가시킬 수 있다")
    void increaseStock() {
        // given
        final Product product = ProductFixture.productWithStock(10);
        int increaseAmount = 5;

        // when
        final Product increased = product.increaseStock(increaseAmount);

        // then
        assertThat(increased.stock().value()).isEqualTo(15);
    }

    @Test
    @DisplayName("품절 상품의 재고를 증가시키면 IN_STOCK 상태가 된다")
    void increaseStockFromZero() {
        // given
        final Product product = ProductFixture.productWithStock(0);

        // when
        final Product increased = product.increaseStock(1);

        // then
        assertThat(increased.stockStatus()).isEqualTo(StockStatus.IN_STOCK);
    }

    @Test
    @DisplayName("판매 준비 중인 상품은 판매를 시작할 수 있다")
    void startSaleFromPreparing() {
        assertSaleStarted(
                ProductFixture.product(),
                ProductStatus.PREPARING
        );
    }

    @Test
    @DisplayName("판매 종료된 상품은 다시 판매를 시작할 수 있다")
    void restartSaleFromSaleEnded() {
        assertSaleStarted(
                ProductFixture.productWithStatus(ProductStatus.SALE_ENDED),
                ProductStatus.SALE_ENDED
        );
    }

    @Test
    @DisplayName("판매 중인 상품은 판매를 종료할 수 있다")
    void endSale() {
        // given
        final Product product = ProductFixture.productWithStatus(ProductStatus.ON_SALE);

        // when
        final Product ended = product.endSale();

        // then
        assertThat(ended.status()).isEqualTo(ProductStatus.SALE_ENDED);
        assertThat(product.status()).isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("판매 중이 아닌 상품은 판매를 종료할 수 없다")
    void cannotEndSaleWithNotOnSale() {
        // given
        final Product product = ProductFixture.product();

        // when & then
        assertThatThrownBy(product::endSale)
                .isInstanceOf(InvalidProductException.class);
    }

    @Test
    @DisplayName("재고 감소 시 원본은 유지되고 새로운 Product가 반환된다")
    void immutability() {
        // given
        final Product original = ProductFixture.productWithStock(10);

        // when
        final Product decreased = original.decreaseStock(3);

        // then
        assertThat(original.stock().value()).isEqualTo(10);
        assertThat(decreased.stock().value()).isEqualTo(7);
        assertThat(decreased).isNotSameAs(original);
    }

    private void assertSaleStarted(
            final Product original,
            final ProductStatus originalStatus
    ) {
        final Product started = original.startSale();

        assertThat(started.status()).isEqualTo(ProductStatus.ON_SALE);
        assertThat(original.status()).isEqualTo(originalStatus);
        assertThat(started).isNotSameAs(original);
    }

}
