package com.dochiri.tooltestlab.order.application.exception;

public final class NotOrderOwnerException extends OrderApplicationException {

    public NotOrderOwnerException() {
        super(OrderApplicationErrorCode.NOT_ORDER_OWNER, "자신의 주문만 조회/취소할 수 있습니다.");
    }
}
