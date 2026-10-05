package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidCartRevisionException extends OrderDomainException {

    public InvalidCartRevisionException() {
        super("장바구니 revision은 음수일 수 없습니다.");
    }

}
