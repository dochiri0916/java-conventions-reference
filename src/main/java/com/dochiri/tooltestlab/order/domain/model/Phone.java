package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;

public record Phone(String value) {

    public Phone {
        if (value == null) {
            throw OrderDomainException.shippingAddressRequired();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw OrderDomainException.badShippingAddress("연락처는 빈 값일 수 없습니다.");
        }
    }
}
