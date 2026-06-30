package com.dochiri.tooltestlab.order.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderAppExceptionTest {

    @Test
    @DisplayName("EmptyCartException 정상 생성")
    void emptyCart() {
            // given

            // when

            // then

        var ex = EmptyCartException.emptyCart();
        assertThat(ex.code()).isEqualTo(OrderApplicationErrorCode.EMPTY_CART);
    }

    @Test
    @DisplayName("NotOrderOwnerException 정상 생성")
    void notOrderOwner() {
            // given

            // when

            // then

        var ex = NotOrderOwnerException.notOrderOwner();
        assertThat(ex.code()).isEqualTo(OrderApplicationErrorCode.NOT_ORDER_OWNER);
    }

    @Test
    @DisplayName("OrderNotFoundException 정상 생성")
    void orderNotFound() {
            // given

            // when

            // then

        var ex = OrderNotFoundException.orderNotFound("order123");
        assertThat(ex.code()).isEqualTo(OrderApplicationErrorCode.ORDER_NOT_FOUND);
        assertThat(ex.publicId()).isEqualTo("order123");
    }

    @Test
    @DisplayName("OrderApplicationErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
            // given

            // when

            // then

        for (var code : OrderApplicationErrorCode.values()) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
