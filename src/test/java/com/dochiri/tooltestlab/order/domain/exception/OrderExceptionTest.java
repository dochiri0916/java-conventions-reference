package com.dochiri.tooltestlab.order.domain.exception;

import com.dochiri.tooltestlab.order.domain.model.OrderId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderExceptionTest {

    @Test
    @DisplayName("InvalidOrderIdException 정상 생성")
    void invalidOrderId() {
            // given

            // when

            // then

        var ex = InvalidOrderIdException.invalidOrderId();
        assertThat(ex.code()).isEqualTo(OrderDomainErrorCode.INVALID_ORDER_ID);
    }

    @Test
    @DisplayName("InvalidOrderLinesException 정상 생성")
    void badOrderLines() {
            // given

            // when

            // then

        var ex = InvalidOrderLinesException.badOrderLines("detail");
        assertThat(ex.code()).isEqualTo(OrderDomainErrorCode.BAD_ORDER_LINES);
        assertThat(ex.detail()).isEqualTo("detail");
    }

    @Test
    @DisplayName("InvalidQuantityException 정상 생성")
    void invalidQuantity() {
            // given

            // when

            // then

        var ex = InvalidQuantityException.invalidQuantity(0);
        assertThat(ex.code()).isEqualTo(OrderDomainErrorCode.INVALID_QUANTITY);
        assertThat(ex.value()).isEqualTo(0);
    }

    @Test
    @DisplayName("InvalidShippingAddressException 정상 생성")
    void badShippingAddr() {
            // given

            // when

            // then

        var ex = InvalidShippingAddressException.badShippingAddr("detail");
        assertThat(ex.code()).isEqualTo(OrderDomainErrorCode.BAD_SHIPPING_ADDR);
        assertThat(ex.detail()).isEqualTo("detail");
    }

    @Test
    @DisplayName("InvalidOrderStatusException 정상 생성")
    void badOrderStatus() {
            // given

            // when

            // then

        var orderId = OrderId.generate();
        var ex = InvalidOrderStatusException.badOrderStatus(orderId, "CANCELLED", "DELIVERED");
        assertThat(ex.code()).isEqualTo(OrderDomainErrorCode.BAD_ORDER_STATUS);
        assertThat(ex.orderId()).isEqualTo(orderId);
        assertThat(ex.expected()).isEqualTo("CANCELLED");
        assertThat(ex.actual()).isEqualTo("DELIVERED");
    }

    @Test
    @DisplayName("OrderDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
            // given

            // when

            // then

        for (var code : OrderDomainErrorCode.values()) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
