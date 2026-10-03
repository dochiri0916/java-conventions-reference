package com.dochiri.tooltestlab.member.domain;

import com.dochiri.tooltestlab.member.domain.vo.Email;
import com.dochiri.tooltestlab.member.domain.vo.EncodedPassword;
import com.dochiri.tooltestlab.member.domain.vo.MemberId;
import com.dochiri.tooltestlab.member.domain.vo.MemberName;
import com.dochiri.tooltestlab.member.domain.vo.MemberStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("회원 생성 시 필수 VO가 null이면 NullPointerException이 발생한다")
    void rejectsNullWhenCreating() {
        // given
        final Email email = Email.of("member@example.com");
        final EncodedPassword password = EncodedPassword.of("encoded-password");
        final MemberName name = MemberName.of("홍길동");

        // when
        // then
        assertThrows(NullPointerException.class, () -> Member.create(null, password, name));
        assertThrows(NullPointerException.class, () -> Member.create(email, null, name));
        assertThrows(NullPointerException.class, () -> Member.create(email, password, null));
    }

    @Test
    @DisplayName("회원 복원 시 모든 필수 필드의 null을 거부한다")
    void rejectsNullWhenRestoring() {
        // given
        final MemberId id = MemberId.generate();
        final Email email = Email.of("member@example.com");
        final EncodedPassword password = EncodedPassword.of("encoded-password");
        final MemberName name = MemberName.of("홍길동");
        final MemberStatus status = MemberStatus.ACTIVE;

        // when
        // then
        assertThrows(NullPointerException.class, () -> Member.restore(null, email, password, name, status));
        assertThrows(NullPointerException.class, () -> Member.restore(id, null, password, name, status));
        assertThrows(NullPointerException.class, () -> Member.restore(id, email, null, name, status));
        assertThrows(NullPointerException.class, () -> Member.restore(id, email, password, null, status));
        assertThrows(NullPointerException.class, () -> Member.restore(id, email, password, name, null));
    }

    @Test
    @DisplayName("유효한 VO로 회원을 생성하고 같은 상태로 복원한다")
    void createsAndRestoresMember() {
        // given
        final Email email = Email.of("member@example.com");
        final EncodedPassword password = EncodedPassword.of("encoded-password");
        final MemberName name = MemberName.of("홍길동");

        // when
        final Member created = Member.create(email, password, name);
        final Member restored = Member.restore(created.id(), email, password, name, created.status());

        // then
        assertEquals(created.id(), restored.id());
        assertSame(email, created.email());
        assertSame(password, created.encodedPassword());
        assertSame(name, created.name());
        assertSame(email, restored.email());
        assertSame(password, restored.encodedPassword());
        assertSame(name, restored.name());
        assertEquals(MemberStatus.ACTIVE, created.status());
        assertEquals(created.status(), restored.status());
    }
}
