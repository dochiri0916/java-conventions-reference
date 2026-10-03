package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.order.domain.exception.ShippingAddressRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class StreetAddressTest {

    @Test
    @DisplayName("주소이 null이면 필수 예외가 발생한다")
    void rejectsNull() {
        // given
        final String value = null;

        // when
        // then
        assertThrows(ShippingAddressRequiredException.class, () -> StreetAddress.of(value));
    }

    @Test
    @DisplayName("주소이 비어 있거나 공백뿐이면 유효성 예외가 발생한다")
    void rejectsBlankValues() {
        // given
        final String[] values = {"", " ", "\t\n", "\u2003"};

        // when
        // then
        for (final String value : values) {
            assertThrows(InvalidShippingAddressException.class, () -> StreetAddress.of(value));
        }
    }

    @Test
    @DisplayName("주소의 양끝 공백을 제거하고 값 기준 동등성을 유지한다")
    void normalizesAndComparesValues() {
        // given
        final StreetAddress value = StreetAddress.of("\u2003 홍길동 \t");
        final StreetAddress same = StreetAddress.of("홍길동");
        final StreetAddress different = StreetAddress.of("김철수");

        // when
        final String stored = value.value();

        // then
        assertEquals("홍길동", stored);
        assertEquals(value, value);
        assertEquals(value, same);
        assertEquals(same, value);
        assertEquals(value.hashCode(), same.hashCode());
        assertNotEquals(value, different);
        assertNotEquals(value, null);
        assertNotEquals(value, stored);
        assertEquals("StreetAddress[value=홍길동]", value.toString());
    }

    @Test
    @DisplayName("주소은 상속과 외부 생성자 호출을 차단하고 상태를 불변으로 유지한다")
    void restrictsConstructionAndMutation() {
        // given
        final Class<StreetAddress> type = StreetAddress.class;

        // when
        final var constructors = type.getDeclaredConstructors();
        final var fields = type.getDeclaredFields();

        // then
        assertTrue(Modifier.isFinal(type.getModifiers()));
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
        for (final var field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                assertTrue(Modifier.isPrivate(field.getModifiers()));
                assertTrue(Modifier.isFinal(field.getModifiers()));
            }
        }
    }
}
