package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException;
import com.dochiri.tooltestlab.order.domain.exception.OrderIdRequiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderIdTest {

    private static final UUID VALID_UUID = UUID.fromString("01900000-0000-7000-8000-000000000001");

    @Test
    @DisplayName("문자열과 UUID가 null이면 주문 ID 필수 예외가 발생한다")
    void rejectsNull() {
        // given
        final String text = null;
        final UUID uuid = null;
        // when
        // then
        assertThrows(OrderIdRequiredException.class, () -> OrderId.parse(text));
        assertThrows(OrderIdRequiredException.class, () -> OrderId.of(uuid));
    }

    @Test
    @DisplayName("빈 문자열과 공백이 포함된 ID는 유효하지 않은 주문 ID 예외가 발생한다")
    void rejectsWhitespace() {
        // given
        final String[] inputs = {"", " ", "\t\n", "\u2003", " " + VALID_UUID,
                VALID_UUID + " ", VALID_UUID + "\n", "01900000-0000-7000-8000-00000000000 "};
        // when
        // then
        for (final String input : inputs) {
            assertThrows(InvalidOrderIdException.class, () -> OrderId.parse(input));
        }
    }

    @Test
    @DisplayName("잘못된 UUID 형식과 축약 및 하이픈 없는 UUID는 커스텀 예외가 발생한다")
    void rejectsMalformedUuid() {
        // given
        final String[] inputs = {"invalid", "1-1-7000-8000-1",
                "01900000-0000-7000-8000-00000000000g", "0190000000007000800000000000000",
                "01900000000070008000000000000001"};
        // when
        // then
        for (final String input : inputs) {
            assertThrows(InvalidOrderIdException.class, () -> OrderId.parse(input));
        }
    }

    @Test
    @DisplayName("UUID v7이 아니면 생성과 문자열 복원을 거부한다")
    void rejectsOtherVersions() {
        // given
        final UUID uuid = UUID.fromString("01900000-0000-4000-8000-000000000001");
        // when
        // then
        assertThrows(InvalidOrderIdException.class, () -> OrderId.of(uuid));
        assertThrows(InvalidOrderIdException.class, () -> OrderId.parse(uuid.toString()));
    }

    @Test
    @DisplayName("표준 형식의 소문자와 대문자 문자열에서 동일한 UUID를 복원한다")
    void parsesSupportedFormats() {
        // given
        final UUID uuid = UUID.fromString("019000ab-cdef-7000-8000-000000000001");
        // when
        final OrderId canonical = OrderId.parse(uuid.toString());
        final OrderId uppercase = OrderId.parse("019000AB-CDEF-7000-8000-000000000001");
        // then
        assertEquals(uuid, canonical.orderId());
        assertEquals(canonical, uppercase);
        assertEquals(canonical.hashCode(), uppercase.hashCode());
    }

    @Test
    @DisplayName("생성된 UUID v7을 표준 36자리 저장 형식으로 변환하고 복원한다")
    void generatesAndRestores() {
        // given
        final OrderId generated = OrderId.generate();
        // when
        final String stored = generated.value();
        final OrderId restored = OrderId.parse(stored);
        // then
        assertEquals(7, generated.orderId().version());
        assertEquals(36, stored.length());
        assertEquals(generated.orderId().toString(), stored);
        assertEquals(generated, restored);
    }

    @Test
    @DisplayName("주문 ID는 상속과 외부 생성자 호출을 차단한다")
    void restrictsConstruction() {
        // given
        final Class<OrderId> type = OrderId.class;
        // when
        final var constructors = type.getDeclaredConstructors();
        // then
        assertTrue(Modifier.isFinal(type.getModifiers()));
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
    }
}
