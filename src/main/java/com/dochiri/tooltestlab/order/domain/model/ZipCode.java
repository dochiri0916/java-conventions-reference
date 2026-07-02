package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;

public record ZipCode(String value) {

    public ZipCode {
        if (value == null) {
            throw InvalidShippingAddressException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidShippingAddressException.badShippingAddr("우편번호는 빈 값일 수 없습니다.");
        }
    }
}
