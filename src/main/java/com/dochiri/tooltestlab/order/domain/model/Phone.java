package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;

public record Phone(String value) {

    public Phone {
        if (value == null) {
            throw InvalidShippingAddressException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidShippingAddressException.badShippingAddr("연락처는 빈 값일 수 없습니다.");
        }
    }
}
