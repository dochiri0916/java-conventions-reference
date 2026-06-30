package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;

import static java.util.Objects.requireNonNull;

public record StreetAddress(String value) {

    public StreetAddress {
        requireNonNull(value, "주소는 null일 수 없습니다.");
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidShippingAddressException.badShippingAddr("주소는 빈 값일 수 없습니다.");
        }
    }
}
