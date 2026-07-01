package com.dochiri.tooltestlab.cart.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartExceptionTest {

    @Test
    @DisplayName("InvalidCartIdException 정상 생성")
    void invalidCartId() {
        // given
        var expectedCode = CartDomainErrorCode.INVALID_CART_ID;

        // when
        var ex = InvalidCartIdException.invalidCartId();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidCartItemException 정상 생성")
    void invalidCartItem() {
        // given
        var detail = "detail";
        var expectedCode = CartDomainErrorCode.INVALID_CART_ITEM;

        // when
        var ex = InvalidCartItemException.invalidCartItem(detail);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.detail()).isEqualTo(detail);
    }

    @Test
    @DisplayName("CartItemLimitExceededException 정상 생성")
    void tooManyItems() {
        // given
        var actualSize = 150;
        var expectedCode = CartDomainErrorCode.TOO_MANY_ITEMS;

        // when
        var ex = CartItemLimitExceededException.tooManyItems(actualSize);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.actualSize()).isEqualTo(actualSize);
    }

    @Test
    @DisplayName("CartDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = CartDomainErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
