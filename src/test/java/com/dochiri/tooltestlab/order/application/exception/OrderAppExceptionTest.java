package com.dochiri.tooltestlab.order.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderAppExceptionTest {

    @Test
    @DisplayName("EmptyCartException 정상 생성")
    void emptyCart() {
        // given
        var expectedCode = OrderApplicationErrorCode.EMPTY_CART;

        // when
        var ex = EmptyCartException.emptyCart();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("NotOrderOwnerException 정상 생성")
    void notOrderOwner() {
        // given
        var expectedCode = OrderApplicationErrorCode.NOT_ORDER_OWNER;

        // when
        var ex = NotOrderOwnerException.notOrderOwner();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("OrderNotFoundException 정상 생성")
    void orderNotFound() {
        // given
        var expectedCode = OrderApplicationErrorCode.ORDER_NOT_FOUND;
        var expectedPublicId = "order123";

        // when
        var ex = OrderNotFoundException.orderNotFound(expectedPublicId);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.publicId()).isEqualTo(expectedPublicId);
    }

    @Test
    @DisplayName("OrderApplicationErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = OrderApplicationErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
