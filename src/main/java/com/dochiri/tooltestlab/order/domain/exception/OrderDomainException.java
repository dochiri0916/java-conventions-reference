package com.dochiri.tooltestlab.order.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;
import com.dochiri.tooltestlab.order.domain.model.OrderId;

import java.util.Map;

public final class OrderDomainException extends DomainException {

    private OrderDomainException(final OrderDomainErrorCode errorCode, final Map<String, ?> extensions) {
        super(errorCode, errorCode.detail(), extensions);
    }

    public static OrderDomainException invalidOrderId() {
        return of(OrderDomainErrorCode.INVALID_ORDER_ID);
    }

    public static OrderDomainException orderIdRequired() {
        return of(OrderDomainErrorCode.ORDER_ID_REQUIRED);
    }

    public static OrderDomainException invalidQuantity(final int value) {
        return new OrderDomainException(OrderDomainErrorCode.INVALID_QUANTITY, Map.of("quantity", value));
    }

    public static OrderDomainException quantityRequired() {
        return of(OrderDomainErrorCode.QUANTITY_REQUIRED);
    }

    public static OrderDomainException badShippingAddress(final String detail) {
        return new OrderDomainException(OrderDomainErrorCode.BAD_SHIPPING_ADDR, Map.of("reason", detail));
    }

    public static OrderDomainException shippingAddressRequired() {
        return of(OrderDomainErrorCode.SHIPPING_ADDRESS_REQUIRED);
    }

    public static OrderDomainException badOrderLines(final String detail) {
        return new OrderDomainException(OrderDomainErrorCode.BAD_ORDER_LINES, Map.of("reason", detail));
    }

    public static OrderDomainException orderLinesRequired() {
        return of(OrderDomainErrorCode.ORDER_LINES_REQUIRED);
    }

    public static OrderDomainException orderStatusRequired() {
        return of(OrderDomainErrorCode.ORDER_STATUS_REQUIRED);
    }

    public static OrderDomainException totalAmountRequired() {
        return of(OrderDomainErrorCode.TOTAL_AMOUNT_REQUIRED);
    }

    public static OrderDomainException productIdRequired() {
        return of(OrderDomainErrorCode.PRODUCT_ID_REQUIRED);
    }

    public static OrderDomainException productNameRequired() {
        return of(OrderDomainErrorCode.PRODUCT_NAME_REQUIRED);
    }

    public static OrderDomainException unitPriceRequired() {
        return of(OrderDomainErrorCode.UNIT_PRICE_REQUIRED);
    }

    public static OrderDomainException badOrderStatus(
            final OrderId orderId, final String expected, final String actual) {
        return new OrderDomainException(
                OrderDomainErrorCode.BAD_ORDER_STATUS,
                Map.of("orderId", orderId.value(), "expectedStatus", expected, "actualStatus", actual)
        );
    }

    private static OrderDomainException of(final OrderDomainErrorCode errorCode) {
        return new OrderDomainException(errorCode, Map.of());
    }
}
