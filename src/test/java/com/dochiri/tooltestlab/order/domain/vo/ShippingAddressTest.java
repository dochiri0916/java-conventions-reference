package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.order.domain.exception.ShippingAddressRequiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShippingAddressTest {

    @Test
    @DisplayName("배송 주소는 모든 필수 VO를 요구하고 값 기준 동등성을 유지한다")
    void validatesAndComparesAddress() {
        // given
        final RecipientName name = RecipientName.of("홍길동");
        final Phone phone = Phone.of("010-1234-5678");
        final StreetAddress street = StreetAddress.of("서울시 주소");
        final ZipCode zipCode = ZipCode.of("12345");

        // when
        final ShippingAddress address = ShippingAddress.of(name, phone, street, zipCode);
        final ShippingAddress same = ShippingAddress.of(name, phone, street, zipCode);

        // then
        assertEquals(name, address.recipientName());
        assertEquals(phone, address.phone());
        assertEquals(street, address.address());
        assertEquals(zipCode, address.zipCode());
        assertEquals(address, same);
        assertEquals(address.hashCode(), same.hashCode());
        assertNotEquals(address, ShippingAddress.of(RecipientName.of("김철수"), phone, street, zipCode));
        assertThrows(ShippingAddressRequiredException.class, () -> ShippingAddress.of(null, phone, street, zipCode));
        assertThrows(ShippingAddressRequiredException.class, () -> ShippingAddress.of(name, null, street, zipCode));
        assertThrows(ShippingAddressRequiredException.class, () -> ShippingAddress.of(name, phone, null, zipCode));
        assertThrows(ShippingAddressRequiredException.class, () -> ShippingAddress.of(name, phone, street, null));
    }
}
