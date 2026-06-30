package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidEmailException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberNameException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberVoTest {

    @Test
    @DisplayName("Email 생성 - 정상")
    void emailValid() {
            // given

            // when

            // then

        var email = new Email("test@example.com");
        assertThat(email.value()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Email 생성 - null이면 NullPointerException")
    void emailNull() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Email 생성 - blank면 예외")
    void emailBlank() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new Email("  "))
                .isInstanceOf(InvalidEmailException.class);
    }

    @Test
    @DisplayName("MemberId 생성 - 정상")
    void memberIdValid() {
            // given

            // when

            // then

        var id = new MemberId("abc123");
        assertThat(id.value()).isEqualTo("abc123");
    }

    @Test
    @DisplayName("MemberId 생성 - null이면 NullPointerException")
    void memberIdNull() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new MemberId(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("MemberId.generate는 UUID 포맷의 값을 생성한다")
    void memberIdGenerate() {
            // given

            // when

            // then

        var id = MemberId.generate();
        assertThat(id.value()).hasSize(32);
        assertThat(id.value()).doesNotContain("-");
    }

    @Test
    @DisplayName("MemberName 생성 - 정상")
    void memberNameValid() {
            // given

            // when

            // then

        var name = new MemberName("홍길동");
        assertThat(name.value()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("MemberName 생성 - null이면 NullPointerException")
    void memberNameNull() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new MemberName(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("MemberName 생성 - blank면 예외")
    void memberNameBlank() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new MemberName("  "))
                .isInstanceOf(InvalidMemberNameException.class);
    }

    @Test
    @DisplayName("Password 생성 - 정상")
    void passwordValid() {
            // given

            // when

            // then

        var pw = new Password("encrypted123");
        assertThat(pw.value()).isEqualTo("encrypted123");
    }

    @Test
    @DisplayName("Password 생성 - null이면 NullPointerException")
    void passwordNull() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new Password(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Password 생성 - blank면 예외")
    void passwordBlank() {
            // given

            // when

            // then

        assertThatThrownBy(() -> new Password("  "))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    @DisplayName("Member 생성 - 정상")
    void memberCreate() {
            // given

            // when

            // then

        var id = MemberId.generate();
        var email = new Email("test@test.com");
        var name = new MemberName("홍길동");
        var password = new Password("enc");
        var member = Member.create(email, password, name);
        assertThat(member.id()).isNotNull();
        assertThat(member.email()).isEqualTo(email);
        assertThat(member.name()).isEqualTo(name);
    }

    @Test
    @DisplayName("Member 생성자는 null을 거부한다")
    void memberNullChecks() {
            // given

            // when

            // then

        var id = MemberId.generate();
        var email = new Email("test@test.com");
        var name = new MemberName("홍길동");
        var password = new Password("enc");
        var status = MemberStatus.ACTIVE;

        assertThatThrownBy(() -> new Member(null, email, password, name, status))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Member(id, null, password, name, status))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Member(id, email, null, name, status))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Member(id, email, password, null, status))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Member(id, email, password, name, null))
                .isInstanceOf(NullPointerException.class);
    }
}
