package com.dochiri.tooltestlab.order.domain.exception;

import com.dochiri.tooltestlab.order.domain.model.OrderId;

import static java.util.Objects.requireNonNull;

public final class InvalidOrderStatusException extends OrderDomainException {

    public InvalidOrderStatusException(final OrderId orderId, final String expected, final String actual) {
        super(
                OrderDomainErrorCode.BAD_ORDER_STATUS,
                "주문[%s]의 상태가 유효하지 않습니다. 예상=%s, 실제=%s".formatted(orderId.value(), expected, actual));
        requireNonNull(orderId);
        requireNonNull(expected);
        requireNonNull(actual);
    }

}
