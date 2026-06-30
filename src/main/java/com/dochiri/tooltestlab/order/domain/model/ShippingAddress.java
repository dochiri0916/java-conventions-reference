package com.dochiri.tooltestlab.order.domain.model;

import static java.util.Objects.requireNonNull;

public record ShippingAddress(RecipientName recipientName, Phone phone, StreetAddress address, ZipCode zipCode) {

    public ShippingAddress {
        requireNonNull(recipientName, "받는 분 성함은 null일 수 없습니다.");
        requireNonNull(phone, "연락처는 null일 수 없습니다.");
        requireNonNull(address, "주소는 null일 수 없습니다.");
        requireNonNull(zipCode, "우편번호는 null일 수 없습니다.");
    }
}
