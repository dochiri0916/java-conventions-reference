package com.dochiri.tooltestlab.global.domain.exception;

public final class InvalidOutboxAttemptsException extends GlobalDomainException {

    public InvalidOutboxAttemptsException() {
        super("Outbox attempts는 음수일 수 없습니다.");
    }

}
