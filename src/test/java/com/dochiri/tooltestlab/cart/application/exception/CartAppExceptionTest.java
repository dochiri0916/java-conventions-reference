package com.dochiri.tooltestlab.cart.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartAppExceptionTest {

    @Test
    @DisplayName("CartNotFoundException 정상 생성")
    void cartNotFound() {
            // given

            // when

            // then

        var ex = CartNotFoundException.cartNotFound("member123");
        assertThat(ex.code()).isEqualTo(CartErrorCode.CART_NOT_FOUND);
        assertThat(ex.memberPublicId()).isEqualTo("member123");
    }

    @Test
    @DisplayName("CartErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
            // given

            // when

            // then

        for (var code : CartErrorCode.values()) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
