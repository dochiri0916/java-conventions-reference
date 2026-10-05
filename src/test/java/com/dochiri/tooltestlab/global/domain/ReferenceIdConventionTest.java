package com.dochiri.tooltestlab.global.domain;

import com.dochiri.tooltestlab.order.domain.vo.CartReferenceId;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ReferenceIdConventionTest {
    @Test
    void newForeignReferencesKeepUuidV7AndCanonicalRepresentation() {
        final String value = "01900000-0000-7000-8000-00000000000a";
        assertEquals(value, CartReferenceId.parse(value.toUpperCase()).value());
        assertEquals(CartReferenceId.parse(value), CartReferenceId.of(UUID.fromString(value)));
        assertThrows(RuntimeException.class, () -> CartReferenceId.parse(null));
        assertThrows(RuntimeException.class, () -> CartReferenceId.parse(UUID.randomUUID().toString()));
        assertThrows(RuntimeException.class, () -> CartReferenceId.parse("1900000-0-7000-8000-a"));
    }
}
