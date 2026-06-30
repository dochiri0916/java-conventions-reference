package com.dochiri.tooltestlab.product.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductExceptionTest {

    @Test
    @DisplayName("InvalidProductIdException 정상 생성")
    void badProductId() {
            // given

            // when

            // then

        var ex = InvalidProductIdException.badProductId();
        assertThat(ex.code()).isEqualTo(ProductDomainErrorCode.BAD_PRODUCT_ID);
    }

    @Test
    @DisplayName("InvalidProductNameException 정상 생성")
    void badProductName() {
            // given

            // when

            // then

        var ex = InvalidProductNameException.badProductName();
        assertThat(ex.code()).isEqualTo(ProductDomainErrorCode.BAD_PRODUCT_NAME);
    }

    @Test
    @DisplayName("InvalidStockException 정상 생성")
    void invalidStock() {
            // given

            // when

            // then

        var ex = InvalidStockException.invalidStock("detail");
        assertThat(ex.code()).isEqualTo(ProductDomainErrorCode.INVALID_STOCK);
        assertThat(ex.detail()).isEqualTo("detail");
    }

    @Test
    @DisplayName("InsufficientStockException 정상 생성")
    void lowStock() {
            // given

            // when

            // then

        var ex = InsufficientStockException.lowStock(5, 10);
        assertThat(ex.code()).isEqualTo(ProductDomainErrorCode.LOW_STOCK);
        assertThat(ex.currentStock()).isEqualTo(5);
        assertThat(ex.requested()).isEqualTo(10);
    }

    @Test
    @DisplayName("InvalidProductException 정상 생성")
    void invalidProduct() {
            // given

            // when

            // then

        var ex = InvalidProductException.invalidProduct();
        assertThat(ex.code()).isEqualTo(ProductDomainErrorCode.INVALID_PRODUCT);
    }

    @Test
    @DisplayName("ProductDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
            // given

            // when

            // then

        for (var code : ProductDomainErrorCode.values()) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
