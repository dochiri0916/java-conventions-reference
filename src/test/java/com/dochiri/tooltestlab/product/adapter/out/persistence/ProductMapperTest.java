package com.dochiri.tooltestlab.product.adapter.out.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductMapperTest {

    @Test
    @DisplayName("ProductMapper는 인스턴스화할 수 없다")
    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    void privateConstructor() throws ReflectiveOperationException {
        // given
        Constructor<ProductMapper> constructor = ProductMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when & then
        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .hasCauseInstanceOf(UnsupportedOperationException.class);
    }
}
