package com.dochiri.tooltestlab.cart.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartExceptionTest {

    @Test
    @DisplayName("CartDomainException - 장바구니 ID 오류")
    void invalidCartId() {
        // given
        var expectedCode = CartDomainErrorCode.INVALID_CART_ID;

        // when
        var ex = CartDomainException.invalidCartId();

        // then
        assertThat(ex.errorCode()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("CartDomainException - 장바구니 ID 필수")
    void cartIdRequired() {
        // given
        var expectedCode = CartDomainErrorCode.CART_ID_REQUIRED;

        // when
        var ex = CartDomainException.cartIdRequired();

        // then
        assertThat(ex.errorCode()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("CartDomainException - 장바구니 상품 오류")
    void invalidCartItem() {
        // given
        var detail = "detail";
        var expectedCode = CartDomainErrorCode.INVALID_CART_ITEM;

        // when
        var ex = CartDomainException.invalidCartItem(detail);

        // then
        assertThat(ex.errorCode()).isEqualTo(expectedCode);
        assertThat(ex.extensions()).containsEntry("reason", detail);
    }

    @Test
    @DisplayName("CartDomainException - 장바구니 상품 필수")
    void cartItemRequired() {
        // given
        var expectedCode = CartDomainErrorCode.CART_ITEM_REQUIRED;

        // when
        var ex = CartDomainException.cartItemRequired();

        // then
        assertThat(ex.errorCode()).isEqualTo(expectedCode);
        assertThat(ex.extensions()).isEmpty();
    }

    @Test
    @DisplayName("CartDomainException - 상품 개수 초과")
    void tooManyItems() {
        // given
        var actualSize = 150;
        var expectedCode = CartDomainErrorCode.TOO_MANY_ITEMS;

        // when
        var ex = CartDomainException.tooManyItems(actualSize);

        // then
        assertThat(ex.errorCode()).isEqualTo(expectedCode);
        assertThat(ex.extensions()).containsEntry("actualSize", actualSize);
    }

    @Test
    @DisplayName("CartDomainException - 장바구니 오류")
    void invalidCart() {
        // given
        var expectedCode = CartDomainErrorCode.INVALID_CART;

        // when
        var ex = CartDomainException.invalidCart();

        // then
        assertThat(ex.errorCode()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("CartDomainException - 장바구니 필수")
    void cartRequired() {
        // given
        var expectedCode = CartDomainErrorCode.CART_REQUIRED;

        // when
        var ex = CartDomainException.cartRequired();

        // then
        assertThat(ex.errorCode()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("CartDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = CartDomainErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.name()).isNotBlank();
        }
    }
}
