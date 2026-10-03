package com.dochiri.tooltestlab.order.domain.exception;

public final class ShippingAddressRequiredException extends OrderDomainException {

    public ShippingAddressRequiredException() {
        super("배송지 정보는 필수입니다.");
    }

}
