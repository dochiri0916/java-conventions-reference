package com.dochiri.tooltestlab.cart.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartAppExceptionTest {

    @Test
    @DisplayName("CartNotFoundException 정상 생성")
    void cartNotFound() {
        // given
        var memberPublicId = "member123";
        var expectedCode = CartErrorCode.CART_NOT_FOUND;

        // when
        var ex = CartNotFoundException.cartNotFound(memberPublicId);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.memberPublicId()).isEqualTo(memberPublicId);
    }

    @Test
    @DisplayName("CartErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = CartErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
