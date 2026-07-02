package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartMapperTest {

    @Test
    @DisplayName("CartMapper는 인스턴스화할 수 없다")
    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    void privateConstructor() throws ReflectiveOperationException {
        // given
        Constructor<CartMapper> constructor = CartMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when & then
        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .hasCauseInstanceOf(UnsupportedOperationException.class);
    }
}
