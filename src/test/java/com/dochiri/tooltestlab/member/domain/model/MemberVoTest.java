package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidEmailException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException;
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
    @DisplayName("Password 생성 - 정상")
    void passwordValid() {
        // given
        var expectedValue = "encrypted123";

        // when
        var pw = new Password(expectedValue);

        // then
        assertThat(pw.value()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("Password 생성 - null이면 InvalidPasswordException")
    void passwordNull() {
        // given
        var expectedException = InvalidPasswordException.class;

        // when & then
        assertThatThrownBy(() -> new Password(null))
                .isInstanceOf(expectedException);
    }

    @Test
    @DisplayName("Password 생성 - blank면 예외")
    void passwordBlank() {
        // given
        var blankPassword = "  ";

        // when & then
        assertThatThrownBy(() -> new Password(blankPassword))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    @DisplayName("Member 생성 - 정상")
    void memberRegister() {
        // given
        var email = new Email("test@test.com");
        var name = new MemberName("홍길동");
        var password = new Password("enc");

        // when
        var member = Member.register(email, password, name);

        // then
        assertThat(member.id()).isNotNull();
        assertThat(member.email()).isEqualTo(email);
        assertThat(member.name()).isEqualTo(name);
    }

    @Test
    @DisplayName("Member 생성자는 null을 거부한다")
    void memberNullChecks() {
        // given
        var id = MemberId.generate();
        var email = new Email("test@test.com");
        var name = new MemberName("홍길동");
        var password = new Password("enc");
        var status = MemberStatus.ACTIVE;

        // when & then
        assertThatThrownBy(() -> new Member(null, email, password, name, status))
                .isInstanceOf(InvalidMemberIdException.class);
        assertThatThrownBy(() -> new Member(id, null, password, name, status))
                .isInstanceOf(InvalidEmailException.class);
        assertThatThrownBy(() -> new Member(id, email, null, name, status))
                .isInstanceOf(InvalidPasswordException.class);
        assertThatThrownBy(() -> new Member(id, email, password, null, status))
                .isInstanceOf(InvalidMemberNameException.class);
        assertThatThrownBy(() -> new Member(id, email, password, name, null))
                .isInstanceOf(InvalidMemberException.class);
    }
}
