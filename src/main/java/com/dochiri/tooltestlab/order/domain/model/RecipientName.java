package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;

public record RecipientName(String value) {

    public RecipientName {
        if (value == null) {
            throw InvalidShippingAddressException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidShippingAddressException.badShippingAddr("받는 분 성함은 빈 값일 수 없습니다.");
        }
    }
}
