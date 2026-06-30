package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;

import static java.util.Objects.requireNonNull;

public record RecipientName(String value) {

    public RecipientName {
        requireNonNull(value, "받는 분 성함은 null일 수 없습니다.");
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidShippingAddressException.badShippingAddr("받는 분 성함은 빈 값일 수 없습니다.");
        }
    }
}
