package com.dochiri.tooltestlab.member.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberAppExceptionTest {

    @Test
    @DisplayName("DuplicateEmailException 정상 생성")
    void duplicateEmail() {
        // given
        var expectedCode = MemberErrorCode.DUPLICATE_EMAIL;
        var expectedEmail = "test@test.com";

        // when
        var ex = DuplicateEmailException.duplicateEmail(expectedEmail);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.email()).isEqualTo(expectedEmail);
    }

    @Test
    @DisplayName("LoginFailedException 정상 생성")
    void loginFailed() {
        // given
        var expectedCode = MemberErrorCode.LOGIN_FAILED;

        // when
        var ex = LoginFailedException.loginFailed();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("MemberNotFoundException 정상 생성")
    void memberNotFound() {
        // given
        var expectedCode = MemberErrorCode.MEMBER_NOT_FOUND;
        var expectedId = "id123";

        // when
        var ex = MemberNotFoundException.memberNotFound(expectedId);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.identifier()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("InvalidCommandException emailRequired 정상 생성")
    void emailRequired() {
        // given
        var expectedCode = MemberErrorCode.EMAIL_REQUIRED;

        // when
        var ex = InvalidCommandException.emailRequired();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidCommandException passwordRequired 정상 생성")
    void passwordRequired() {
        // given
        var expectedCode = MemberErrorCode.PASSWORD_REQUIRED;

        // when
        var ex = InvalidCommandException.passwordRequired();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidCommandException nameRequired 정상 생성")
    void nameRequired() {
        // given
        var expectedCode = MemberErrorCode.NAME_REQUIRED;

        // when
        var ex = InvalidCommandException.nameRequired();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }
}
