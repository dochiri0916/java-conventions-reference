package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;

public record StreetAddress(String value) {

    public StreetAddress {
        if (value == null) {
            throw OrderDomainException.shippingAddressRequired();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw OrderDomainException.badShippingAddress("주소는 빈 값일 수 없습니다.");
        }
    }
}
