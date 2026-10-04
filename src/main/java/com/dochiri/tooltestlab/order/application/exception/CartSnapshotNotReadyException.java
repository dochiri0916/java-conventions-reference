package com.dochiri.tooltestlab.order.application.exception;

public final class CartSnapshotNotReadyException extends OrderApplicationException {
    public CartSnapshotNotReadyException() {
        super("장바구니 정보가 아직 동기화되지 않았습니다.");
    }
}
