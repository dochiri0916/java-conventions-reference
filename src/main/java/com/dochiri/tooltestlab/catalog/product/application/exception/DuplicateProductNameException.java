package com.dochiri.tooltestlab.catalog.product.application.exception;

public final class DuplicateProductNameException extends ProductApplicationException {

    public DuplicateProductNameException() {
        super("이미 사용 중인 상품명입니다.");
    }

}
