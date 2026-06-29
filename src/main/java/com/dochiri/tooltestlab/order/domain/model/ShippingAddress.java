package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;

import static java.util.Objects.requireNonNull;

public record ShippingAddress(String recipientName, String phone, String address, String zipCode) {

    public ShippingAddress {
        requireNonNull(recipientName, "받는 분 성함은 null일 수 없습니다.");
        requireNonNull(phone, "연락처는 null일 수 없습니다.");
        requireNonNull(address, "주소는 null일 수 없습니다.");
        requireNonNull(zipCode, "우편번호는 null일 수 없습니다.");

        recipientName = recipientName.strip();
        phone = phone.strip();
        address = address.strip();
        zipCode = zipCode.strip();

        if (recipientName.isBlank()) {
            throw new InvalidShippingAddressException("받는 분 성함은 빈 값일 수 없습니다.");
        }
        if (phone.isBlank()) {
            throw new InvalidShippingAddressException("연락처는 빈 값일 수 없습니다.");
        }
        if (address.isBlank()) {
            throw new InvalidShippingAddressException("주소는 빈 값일 수 없습니다.");
        }
        if (zipCode.isBlank()) {
            throw new InvalidShippingAddressException("우편번호는 빈 값일 수 없습니다.");
        }
    }
}
