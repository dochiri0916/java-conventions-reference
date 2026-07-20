package com.dochiri.tooltestlab.order.application.exception;

import com.dochiri.tooltestlab.global.exception.ApplicationException;

import java.util.Map;

public final class OrderApplicationException extends ApplicationException {

    private OrderApplicationException(final OrderApplicationErrorCode errorCode, final Map<String, ?> extensions) {
        super(errorCode, errorCode.detail(), extensions);
    }

    public static OrderApplicationException orderNotFound(final String orderId) {
        return new OrderApplicationException(OrderApplicationErrorCode.ORDER_NOT_FOUND, Map.of("orderId", orderId));
    }
    public static OrderApplicationException notOrderOwner() { return of(OrderApplicationErrorCode.NOT_ORDER_OWNER); }
    public static OrderApplicationException emptyCart() { return of(OrderApplicationErrorCode.EMPTY_CART); }

    private static OrderApplicationException of(final OrderApplicationErrorCode errorCode) {
        return new OrderApplicationException(errorCode, Map.of());
    }
}
