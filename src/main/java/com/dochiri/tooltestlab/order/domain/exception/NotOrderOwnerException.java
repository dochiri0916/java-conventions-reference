package com.dochiri.tooltestlab.order.domain.exception;

public final class NotOrderOwnerException extends OrderDomainException {

    public NotOrderOwnerException() {
        super("주문을 변경할 권한이 없습니다.");
    }

}
