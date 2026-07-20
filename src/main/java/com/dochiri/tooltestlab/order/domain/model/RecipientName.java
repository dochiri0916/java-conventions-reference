package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;

public record RecipientName(String value) {

    public RecipientName {
        if (value == null) {
            throw OrderDomainException.shippingAddressRequired();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw OrderDomainException.badShippingAddress("받는 분 성함은 빈 값일 수 없습니다.");
        }
    }
}
