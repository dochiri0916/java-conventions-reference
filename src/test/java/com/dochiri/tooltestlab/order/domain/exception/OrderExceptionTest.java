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
        var expectedCode = OrderDomainErrorCode.INVALID_ORDER_ID;

        // when
        var ex = InvalidOrderIdException.invalidOrderId();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidOrderIdException required 정상 생성")
    void orderIdRequired() {
        // given
        var expectedCode = OrderDomainErrorCode.ORDER_ID_REQUIRED;

        // when
        var ex = InvalidOrderIdException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidOrderLinesException 정상 생성")
    void badOrderLines() {
        // given
        var expectedCode = OrderDomainErrorCode.BAD_ORDER_LINES;
        var expectedDetail = "detail";

        // when
        var ex = InvalidOrderLinesException.badOrderLines(expectedDetail);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.detail()).isEqualTo(expectedDetail);
    }

    @Test
    @DisplayName("InvalidOrderLinesException required 정상 생성")
    void orderLinesRequired() {
        // given
        var expectedCode = OrderDomainErrorCode.ORDER_LINES_REQUIRED;

        // when
        var ex = InvalidOrderLinesException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.detail()).isNull();
    }

    @Test
    @DisplayName("InvalidQuantityException 정상 생성")
    void invalidQuantity() {
        // given
        var expectedCode = OrderDomainErrorCode.INVALID_QUANTITY;
        var expectedValue = 0;

        // when
        var ex = InvalidQuantityException.invalidQuantity(expectedValue);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.value()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("InvalidQuantityException required 정상 생성")
    void quantityRequired() {
        // given
        var expectedCode = OrderDomainErrorCode.QUANTITY_REQUIRED;

        // when
        var ex = InvalidQuantityException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.value()).isEqualTo(0);
    }

    @Test
    @DisplayName("InvalidShippingAddressException 정상 생성")
    void badShippingAddr() {
        // given
        var expectedCode = OrderDomainErrorCode.BAD_SHIPPING_ADDR;
        var expectedDetail = "detail";

        // when
        var ex = InvalidShippingAddressException.badShippingAddr(expectedDetail);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.detail()).isEqualTo(expectedDetail);
    }

    @Test
    @DisplayName("InvalidShippingAddressException required 정상 생성")
    void shippingAddrRequired() {
        // given
        var expectedCode = OrderDomainErrorCode.SHIPPING_ADDRESS_REQUIRED;

        // when
        var ex = InvalidShippingAddressException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.detail()).isNull();
    }

    @Test
    @DisplayName("InvalidOrderStatusException 정상 생성")
    void badOrderStatus() {
        // given
        var expectedCode = OrderDomainErrorCode.BAD_ORDER_STATUS;
        var orderId = OrderId.generate();

        // when
        var ex = InvalidOrderStatusException.badOrderStatus(orderId, "CANCELLED", "DELIVERED");

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.orderId()).isEqualTo(orderId);
        assertThat(ex.expected()).isEqualTo("CANCELLED");
        assertThat(ex.actual()).isEqualTo("DELIVERED");
    }

    @Test
    @DisplayName("InvalidOrderStatusException required 정상 생성")
    void orderStatusRequired() {
        // given
        var expectedCode = OrderDomainErrorCode.ORDER_STATUS_REQUIRED;

        // when
        var ex = InvalidOrderStatusException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.orderId()).isNull();
        assertThat(ex.expected()).isNull();
        assertThat(ex.actual()).isNull();
    }

    @Test
    @DisplayName("OrderDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = OrderDomainErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
