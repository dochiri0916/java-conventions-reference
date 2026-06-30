package com.dochiri.tooltestlab.product.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductAppExceptionTest {

    @Test
    @DisplayName("ProductNotFoundException 정상 생성")
    void productNotFound() {
            // given

            // when

            // then

        var ex = ProductNotFoundException.productNotFound("prod123");
        assertThat(ex.code()).isEqualTo(ProductErrorCode.PRODUCT_NOT_FOUND);
        assertThat(ex.productPublicId()).isEqualTo("prod123");
    }

    @Test
    @DisplayName("ProductErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
            // given

            // when

            // then

        for (var code : ProductErrorCode.values()) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
