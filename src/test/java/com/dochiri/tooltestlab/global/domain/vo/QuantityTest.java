package com.dochiri.tooltestlab.global.domain.vo;

import com.dochiri.tooltestlab.global.domain.exception.InvalidQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuantityTest {

    @Test
    @DisplayName("수량은 양수만 허용하고 값 기준 동등성을 유지한다")
    void validates_and_compares_values() {
        // given
        final Quantity quantity = Quantity.of(1);

        // when
        final Quantity same = Quantity.of(1);
        final Quantity maximum = Quantity.of(Integer.MAX_VALUE);

        // then
        assertEquals(1, quantity.value());
        assertEquals(Integer.MAX_VALUE, maximum.value());
        assertEquals(quantity, same);
        assertEquals(quantity.hashCode(), same.hashCode());
        assertNotEquals(quantity, maximum);
        assertNotEquals(quantity, null);
        assertNotEquals(quantity, 1);
        assertEquals("Quantity[value=1]", quantity.toString());
        assertThrows(InvalidQuantityException.class, () -> Quantity.of(0));
        assertThrows(InvalidQuantityException.class, () -> Quantity.of(-1));
    }
}
