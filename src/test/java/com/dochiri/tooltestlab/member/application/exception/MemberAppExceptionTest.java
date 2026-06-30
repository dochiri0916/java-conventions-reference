package com.dochiri.tooltestlab.member.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberAppExceptionTest {

    @Test
    @DisplayName("DuplicateEmailException 정상 생성")
    void duplicateEmail() {
            // given

            // when

            // then

        var ex = DuplicateEmailException.duplicateEmail("test@test.com");
        assertThat(ex.code()).isEqualTo(MemberErrorCode.DUPLICATE_EMAIL);
        assertThat(ex.email()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("LoginFailedException 정상 생성")
    void loginFailed() {
            // given

            // when

            // then

        var ex = LoginFailedException.loginFailed();
        assertThat(ex.code()).isEqualTo(MemberErrorCode.LOGIN_FAILED);
    }

    @Test
    @DisplayName("MemberNotFoundException 정상 생성")
    void memberNotFound() {
            // given

            // when

            // then

        var ex = MemberNotFoundException.memberNotFound("id123");
        assertThat(ex.code()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
        assertThat(ex.identifier()).isEqualTo("id123");
    }
}
