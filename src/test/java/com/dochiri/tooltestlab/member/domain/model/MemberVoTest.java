package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberVoTest {

    @Test
    @DisplayName("Email 생성 - 정상")
    void emailValid() {
        // given
        var expectedValue = "test@example.com";

        // when
        var email = new Email(expectedValue);

        // then
        assertThat(email.value()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("Email 생성 - null이면 InvalidEmailException")
    void emailNull() {
        // given
        var expectedException = InvalidEmailException.class;

        // when & then
        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(expectedException);
    }

    @Test
    @DisplayName("Email 생성 - blank면 예외")
    void emailBlank() {
        // given
        var blankEmail = "  ";

        // when & then
        assertThatThrownBy(() -> new Email(blankEmail))
                .isInstanceOf(InvalidEmailException.class);
    }

    @Test
    @DisplayName("Email 생성 - 형식이 올바르지 않으면 예외")
    void emailInvalidFormat() {
        // given
        var invalidEmail = "invalid-email";

        // when & then
        assertThatThrownBy(() -> new Email(invalidEmail))
                .isInstanceOf(InvalidEmailException.class);
    }

    @Test
    @DisplayName("MemberId 생성 - 정상")
    void memberIdValid() {
        // given
        var expectedValue = "abc123";

        // when
        var id = new MemberId(expectedValue);

        // then
        assertThat(id.value()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("MemberId 생성 - null이면 InvalidMemberIdException")
    void memberIdNull() {
        // given
        var expectedException = InvalidMemberIdException.class;

        // when & then
        assertThatThrownBy(() -> new MemberId(null))
                .isInstanceOf(expectedException);
    }

    @Test
    @DisplayName("MemberId.generate는 UUID 포맷의 값을 생성한다")
    void memberIdGenerate() {
        // given
        var expectedSize = 32;

        // when
        var id = MemberId.generate();

        // then
        assertThat(id.value()).hasSize(expectedSize);
        assertThat(id.value()).doesNotContain("-");
    }

    @Test
    @DisplayName("MemberName 생성 - 정상")
    void memberNameValid() {
        // given
        var expectedValue = "홍길동";

        // when
        var name = new MemberName(expectedValue);

        // then
        assertThat(name.value()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("MemberName 생성 - null이면 InvalidMemberNameException")
    void memberNameNull() {
        // given
        var expectedException = InvalidMemberNameException.class;

        // when & then
        assertThatThrownBy(() -> new MemberName(null))
                .isInstanceOf(expectedException);
    }

    @Test
    @DisplayName("MemberName 생성 - blank면 예외")
    void memberNameBlank() {
        // given
        var blankName = "  ";

        // when & then
        assertThatThrownBy(() -> new MemberName(blankName))
                .isInstanceOf(InvalidMemberNameException.class);
    }

    @Test
    @DisplayName("암호화 비밀번호 생성 - 정상")
    void encodedPasswordValid() {
        // given
        var expectedValue = "encrypted123";

        // when
        var encodedPassword = new EncodedPassword(expectedValue);

        // then
        assertThat(encodedPassword.value()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("암호화 비밀번호 생성 - null이면 InvalidPasswordException")
    void encodedPasswordNull() {
        // given
        var expectedException = InvalidPasswordException.class;

        // when & then
        assertThatThrownBy(() -> new EncodedPassword(null))
                .isInstanceOf(expectedException);
    }

    @Test
    @DisplayName("암호화 비밀번호 생성 - blank면 예외")
    void encodedPasswordBlank() {
        // given
        var blankPassword = "  ";

        // when & then
        assertThatThrownBy(() -> new EncodedPassword(blankPassword))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    @DisplayName("Member 생성 - 정상")
    void memberRegister() {
        // given
        var email = new Email("test@test.com");
        var name = new MemberName("홍길동");
        var encodedPassword = new EncodedPassword("enc");

        // when
        var member = Member.register(email, encodedPassword, name);

        // then
        assertThat(member.id()).isNotNull();
        assertThat(member.email()).isEqualTo(email);
        assertThat(member.name()).isEqualTo(name);
        assertThat(member.encodedPassword()).isEqualTo(encodedPassword);
    }

    @Test
    @DisplayName("Member 생성자는 null을 거부한다")
    void memberNullChecks() {
        // given
        var id = MemberId.generate();
        var email = new Email("test@test.com");
        var name = new MemberName("홍길동");
        var encodedPassword = new EncodedPassword("enc");
        var status = MemberStatus.ACTIVE;

        // when & then
        assertThatThrownBy(() -> new Member(null, email, encodedPassword, name, status))
                .isInstanceOf(InvalidMemberIdException.class);
        assertThatThrownBy(() -> new Member(id, null, encodedPassword, name, status))
                .isInstanceOf(InvalidEmailException.class);
        assertThatThrownBy(() -> new Member(id, email, null, name, status))
                .isInstanceOf(InvalidPasswordException.class);
        assertThatThrownBy(() -> new Member(id, email, encodedPassword, null, status))
                .isInstanceOf(InvalidMemberNameException.class);
        assertThatThrownBy(() -> new Member(id, email, encodedPassword, name, null))
                .isInstanceOf(InvalidMemberException.class);
    }
}
