package com.dochiri.tooltestlab.cart.application.exception;

public final class ProductSnapshotNotReadyException extends CartApplicationException {
    public ProductSnapshotNotReadyException() {
        super("상품 정보가 아직 동기화되지 않았습니다.");
    }
}
