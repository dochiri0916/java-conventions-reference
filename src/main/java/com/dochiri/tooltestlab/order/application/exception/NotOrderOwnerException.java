package com.dochiri.tooltestlab.order.application.exception;

public final class NotOrderOwnerException extends OrderApplicationException {

    public NotOrderOwnerException() {
        super("주문을 변경할 권한이 없습니다.");
    }

}
