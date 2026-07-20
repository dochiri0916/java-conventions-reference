package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.MemberDomainException;
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
        var expectedException = MemberDomainException.class;

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
                .isInstanceOf(MemberDomainException.class);
    }

    @Test
    @DisplayName("Email 생성 - 형식이 올바르지 않으면 예외")
    void emailInvalidFormat() {
        // given
        var invalidEmail = "invalid-email";

        // when & then
        assertThatThrownBy(() -> new Email(invalidEmail))
                .isInstanceOf(MemberDomainException.class);
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
        var expectedException = MemberDomainException.class;

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
        var expectedException = MemberDomainException.class;

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
                .isInstanceOf(MemberDomainException.class);
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
        var expectedException = MemberDomainException.class;

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
                .isInstanceOf(MemberDomainException.class);
    }

    @Test
    @DisplayName("Member 생성 - 정상")
    void memberRegister() {
        // given
        var email = new Email("test@test.com");
        var name = new MemberName("홍길동");
        var encodedPassword = new EncodedPassword("enc");

        // when
        var member = Member.create(email, encodedPassword, name);

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
        assertThatThrownBy(() -> Member.restore(null, email, encodedPassword, name, status))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Member.restore(id, null, encodedPassword, name, status))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Member.restore(id, email, null, name, status))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Member.restore(id, email, encodedPassword, null, status))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Member.restore(id, email, encodedPassword, name, null))
                .isInstanceOf(NullPointerException.class);
    }
}
