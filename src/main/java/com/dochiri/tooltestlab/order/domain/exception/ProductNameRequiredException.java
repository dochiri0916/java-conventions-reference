package com.dochiri.tooltestlab.order.domain.exception;

public final class ProductNameRequiredException extends OrderDomainException {

    public ProductNameRequiredException() {
        super("상품명은 필수입니다.");
    }

}
