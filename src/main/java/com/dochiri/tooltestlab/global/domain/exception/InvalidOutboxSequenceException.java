package com.dochiri.tooltestlab.global.domain.exception;

public final class InvalidOutboxSequenceException extends GlobalDomainException {

    public InvalidOutboxSequenceException() {
        super("Outbox sequence는 음수일 수 없습니다.");
    }

}
