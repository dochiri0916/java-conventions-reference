package com.dochiri.tooltestlab.product.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductAppExceptionTest {

    @Test
    @DisplayName("ProductNotFoundException 정상 생성")
    void productNotFound() {
        // given
        var expectedCode = ProductErrorCode.PRODUCT_NOT_FOUND;
        var expectedPublicId = "prod123";

        // when
        var ex = ProductNotFoundException.productNotFound(expectedPublicId);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.productId()).isEqualTo(expectedPublicId);
    }

    @Test
    @DisplayName("ProductErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = ProductErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.title()).isNotBlank();
        }
    }

    @Test
    @DisplayName("InvalidCommandException 정상 생성")
    void invalidCommand() {
        // given
        var expectedCode = ProductErrorCode.INVALID_INPUT;
        var expectedDetail = "detail";

        // when
        var ex = InvalidCommandException.invalidInput(expectedDetail);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.detail()).isEqualTo(expectedDetail);
    }
}
