package com.dochiri.tooltestlab.member.adapter.out.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberMapperTest {

    @Test
    @DisplayName("MemberMapper는 인스턴스화할 수 없다")
    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    void privateConstructor() throws ReflectiveOperationException {
        // given
        Constructor<MemberMapper> constructor = MemberMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when & then
        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .hasCauseInstanceOf(UnsupportedOperationException.class);
    }
}
