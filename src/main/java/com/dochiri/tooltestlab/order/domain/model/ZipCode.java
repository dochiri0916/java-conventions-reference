package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;

public record ZipCode(String value) {

    public ZipCode {
        if (value == null) {
            throw OrderDomainException.shippingAddressRequired();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw OrderDomainException.badShippingAddress("우편번호는 빈 값일 수 없습니다.");
        }
    }
}
