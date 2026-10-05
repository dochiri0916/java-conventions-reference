package com.dochiri.tooltestlab.global.domain;

import org.junit.jupiter.api.DisplayName;
import com.dochiri.tooltestlab.order.domain.vo.CartReferenceId;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReferenceIdConventionTest {
    @Test
    @DisplayName("새 외부 참조는 UUID v7과 표준 표현을 유지한다")
    void new_foreign_references_keep_uuid_v7_and_canonical_representation() {
        // given
        final String value = "01900000-0000-7000-8000-00000000000a";

        // when & then
        assertEquals(value, CartReferenceId.parse(value.toUpperCase()).value());
        assertEquals(CartReferenceId.parse(value), CartReferenceId.of(UUID.fromString(value)));
        assertThrows(RuntimeException.class, () -> CartReferenceId.parse(null));
        assertThrows(RuntimeException.class, () -> CartReferenceId.parse(UUID.randomUUID().toString()));
        assertThrows(RuntimeException.class, () -> CartReferenceId.parse("1900000-0-7000-8000-a"));
    }
}
