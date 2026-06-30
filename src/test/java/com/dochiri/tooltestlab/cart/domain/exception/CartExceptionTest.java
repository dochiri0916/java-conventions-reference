package com.dochiri.tooltestlab.cart.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartExceptionTest {

    @Test
    @DisplayName("InvalidCartIdException 정상 생성")
    void invalidCartId() {
            // given

            // when

            // then

        var ex = InvalidCartIdException.invalidCartId();
        assertThat(ex.code()).isEqualTo(CartDomainErrorCode.INVALID_CART_ID);
    }

    @Test
    @DisplayName("InvalidCartItemException 정상 생성")
    void invalidCartItem() {
            // given

            // when

            // then

        var ex = InvalidCartItemException.invalidCartItem("detail");
        assertThat(ex.code()).isEqualTo(CartDomainErrorCode.INVALID_CART_ITEM);
        assertThat(ex.detail()).isEqualTo("detail");
    }

    @Test
    @DisplayName("CartItemLimitExceededException 정상 생성")
    void tooManyItems() {
            // given

            // when

            // then

        var ex = CartItemLimitExceededException.tooManyItems(150);
        assertThat(ex.code()).isEqualTo(CartDomainErrorCode.TOO_MANY_ITEMS);
        assertThat(ex.actualSize()).isEqualTo(150);
    }

    @Test
    @DisplayName("CartDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
            // given

            // when

            // then

        for (var code : CartDomainErrorCode.values()) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
