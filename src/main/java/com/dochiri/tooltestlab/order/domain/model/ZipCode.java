package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;

import static java.util.Objects.requireNonNull;

public record ZipCode(String value) {

    public ZipCode {
        requireNonNull(value, "우편번호는 null일 수 없습니다.");
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidShippingAddressException.badShippingAddr("우편번호는 빈 값일 수 없습니다.");
        }
    }
}
