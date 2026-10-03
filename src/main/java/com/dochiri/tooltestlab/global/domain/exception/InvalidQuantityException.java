package com.dochiri.tooltestlab.global.domain.exception;

public final class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException() {
        super("수량은 1 이상이어야 합니다.");
    }

}
