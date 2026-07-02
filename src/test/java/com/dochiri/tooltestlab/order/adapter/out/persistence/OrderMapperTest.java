package com.dochiri.tooltestlab.order.adapter.out.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderMapperTest {

    @Test
    @DisplayName("OrderMapper는 인스턴스화할 수 없다")
    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    void privateConstructor() throws ReflectiveOperationException {
        // given
        Constructor<OrderMapper> constructor = OrderMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when & then
        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .hasCauseInstanceOf(UnsupportedOperationException.class);
    }
}
