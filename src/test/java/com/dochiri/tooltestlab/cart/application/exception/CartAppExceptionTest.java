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
        assertThat(ex.memberId()).isEqualTo(memberPublicId);
    }

    @Test
    @DisplayName("CartErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = CartErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.name()).isNotBlank();
        }
    }

    @Test
    @DisplayName("InvalidCommandException 정상 생성")
    void invalidCommand() {
        // given
        var expectedCode = CartErrorCode.INVALID_INPUT;
        var expectedDetail = "detail";

        // when
        var ex = InvalidCommandException.invalidInput(expectedDetail);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.detail()).isEqualTo(expectedDetail);
    }
}
