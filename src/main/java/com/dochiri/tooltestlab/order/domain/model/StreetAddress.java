package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;

public record StreetAddress(String value) {

    public StreetAddress {
        if (value == null) {
            throw InvalidShippingAddressException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidShippingAddressException.badShippingAddr("주소는 빈 값일 수 없습니다.");
        }
    }
}
