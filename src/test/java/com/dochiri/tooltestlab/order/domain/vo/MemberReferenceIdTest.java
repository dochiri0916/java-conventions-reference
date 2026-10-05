package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.order.domain.exception.InvalidMemberReferenceIdException;
import com.dochiri.tooltestlab.order.domain.exception.MemberReferenceIdRequiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemberReferenceIdTest {

    private static final UUID VALID_UUID = UUID.fromString("01900000-0000-7000-8000-000000000001");

    @Test
    @DisplayName("문자열과 UUID가 null이면 회원 참조 ID 필수 예외가 발생한다")
    void rejects_null() {
        // given
        final String text = null;
        final UUID uuid = null;

        // when & then
        assertThrows(MemberReferenceIdRequiredException.class, () -> MemberReferenceId.parse(text));
        assertThrows(MemberReferenceIdRequiredException.class, () -> MemberReferenceId.of(uuid));
    }

    @Test
    @DisplayName("빈 문자열과 공백이 포함된 ID는 유효하지 않은 회원 참조 ID 예외가 발생한다")
    void rejects_whitespace() {
        // given
        final String[] inputs = {"", " ", "\t\n", "\u2003", " " + VALID_UUID,
                VALID_UUID + " ", VALID_UUID + "\n", "01900000-0000-7000-8000-00000000000 "};

        // when & then
        for (final String input : inputs) {
            assertThrows(InvalidMemberReferenceIdException.class, () -> MemberReferenceId.parse(input));
        }
    }

    @Test
    @DisplayName("잘못된 UUID 형식과 축약 및 하이픈 없는 UUID는 커스텀 예외가 발생한다")
    void rejects_malformed_uuid() {
        // given
        final String[] inputs = {"invalid", "1-1-7000-8000-1",
                "01900000-0000-7000-8000-00000000000g", "0190000000007000800000000000000",
                "01900000000070008000000000000001"};

        // when & then
        for (final String input : inputs) {
            assertThrows(InvalidMemberReferenceIdException.class, () -> MemberReferenceId.parse(input));
        }
    }

    @Test
    @DisplayName("UUID v7이 아니면 생성과 문자열 복원을 거부한다")
    void rejects_other_versions() {
        // given
        final UUID uuid = UUID.fromString("01900000-0000-4000-8000-000000000001");

        // when & then
        assertThrows(InvalidMemberReferenceIdException.class, () -> MemberReferenceId.of(uuid));
        assertThrows(InvalidMemberReferenceIdException.class, () -> MemberReferenceId.parse(uuid.toString()));
    }

    @Test
    @DisplayName("표준 형식의 소문자와 대문자 문자열에서 동일한 UUID를 복원한다")
    void parses_supported_formats() {
        // given
        final UUID uuid = UUID.fromString("019000ab-cdef-7000-8000-000000000001");
        // when
        final MemberReferenceId canonical = MemberReferenceId.parse(uuid.toString());
        final MemberReferenceId uppercase = MemberReferenceId.parse("019000AB-CDEF-7000-8000-000000000001");
        // then
        assertEquals(uuid, canonical.memberId());
        assertEquals(canonical, uppercase);
        assertEquals(canonical.hashCode(), uppercase.hashCode());
    }

    @Test
    @DisplayName("참조 UUID v7은 표준 36자리 문자열로 변환되고 동일한 값으로 복원된다")
    void serializes_and_restores() {
        // given
        final MemberReferenceId reference = MemberReferenceId.of(VALID_UUID);

        // when
        final String stored = reference.value();
        final MemberReferenceId restored = MemberReferenceId.parse(stored);

        // then
        assertEquals(7, reference.memberId().version());
        assertEquals(36, stored.length());
        assertEquals(VALID_UUID.toString(), stored);
        assertEquals(reference, restored);
        assertNotEquals(reference, null);
        assertNotEquals(reference, stored);
        assertEquals("MemberReferenceId[value=" + stored + "]", reference.toString());
    }

    @Test
    @DisplayName("회원 참조 ID는 상속과 외부 생성자 호출을 차단한다")
    void restricts_construction() {
        // given
        final Class<MemberReferenceId> type = MemberReferenceId.class;
        // when
        final var constructors = type.getDeclaredConstructors();
        // then
        assertTrue(Modifier.isFinal(type.getModifiers()));
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
    }
}
