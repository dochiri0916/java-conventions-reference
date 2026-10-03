package com.dochiri.tooltestlab.member.domain.vo;

import com.dochiri.tooltestlab.member.domain.exception.EmailRequiredException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    @DisplayName("이메일이 null이면 필수 예외가 발생한다")
    void rejectsNull() {
        // given
        final String value = null;

        // when
        // then
        assertThrows(EmailRequiredException.class, () -> Email.of(value));
    }

    @Test
    @DisplayName("이메일이 비어 있거나 공백뿐이면 유효성 예외가 발생한다")
    void rejectsBlankValues() {
        // given
        final String[] values = {"", " ", "\t\n", "\u2003"};

        // when
        // then
        for (final String value : values) {
            assertThrows(InvalidEmailException.class, () -> Email.of(value));
        }
    }

    @Test
    @DisplayName("잘못된 이메일 형식과 내부 공백을 거부한다")
    void rejectsMalformedEmail() {
        // given
        final String[] values = {"invalid", "member@example", "@example.com", "member@.com",
                "member name@example.com", "member@@example.com"};

        // when
        // then
        for (final String value : values) {
            assertThrows(InvalidEmailException.class, () -> Email.of(value));
        }
    }

    @Test
    @DisplayName("이메일의 양끝 공백을 제거하고 값 기준 동등성을 유지한다")
    void normalizesAndComparesValues() {
        // given
        final Email value = Email.of("\u2003 member@example.com \t");
        final Email same = Email.of("member@example.com");
        final Email different = Email.of("other@example.com");

        // when
        final String stored = value.value();

        // then
        assertEquals("member@example.com", stored);
        assertEquals(value, value);
        assertEquals(value, same);
        assertEquals(same, value);
        assertEquals(value.hashCode(), same.hashCode());
        assertNotEquals(value, different);
        assertNotEquals(value, null);
        assertNotEquals(value, stored);
        assertEquals("Email[value=member@example.com]", value.toString());
    }

    @Test
    @DisplayName("이메일은 상속과 외부 생성자 호출을 차단하고 상태를 불변으로 유지한다")
    void restrictsConstructionAndMutation() {
        // given
        final Class<Email> type = Email.class;

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
