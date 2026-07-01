package com.dochiri.tooltestlab.product.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductExceptionTest {

    @Test
    @DisplayName("InvalidProductIdException 정상 생성")
    void badProductId() {
        // given
        var expectedCode = ProductDomainErrorCode.BAD_PRODUCT_ID;

        // when
        var ex = InvalidProductIdException.badProductId();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidProductNameException 정상 생성")
    void badProductName() {
        // given
        var expectedCode = ProductDomainErrorCode.BAD_PRODUCT_NAME;

        // when
        var ex = InvalidProductNameException.badProductName();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidStockException 정상 생성")
    void invalidStock() {
        // given
        var expectedCode = ProductDomainErrorCode.INVALID_STOCK;
        var expectedDetail = "detail";

        // when
        var ex = InvalidStockException.invalidStock(expectedDetail);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.detail()).isEqualTo(expectedDetail);
    }

    @Test
    @DisplayName("InsufficientStockException 정상 생성")
    void lowStock() {
        // given
        var expectedCode = ProductDomainErrorCode.LOW_STOCK;
        var currentStock = 5;
        var requested = 10;

        // when
        var ex = InsufficientStockException.lowStock(currentStock, requested);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.currentStock()).isEqualTo(currentStock);
        assertThat(ex.requested()).isEqualTo(requested);
    }

    @Test
    @DisplayName("InvalidProductException 정상 생성")
    void invalidProduct() {
        // given
        var expectedCode = ProductDomainErrorCode.INVALID_PRODUCT;

        // when
        var ex = InvalidProductException.invalidProduct();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("ProductDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = ProductDomainErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
