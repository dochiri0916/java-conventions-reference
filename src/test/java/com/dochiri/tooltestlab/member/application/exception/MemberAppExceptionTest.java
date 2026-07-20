package com.dochiri.tooltestlab.member.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberAppExceptionTest {

    @Test
    @DisplayName("회원 애플리케이션 예외는 중복 이메일 맥락을 보관한다")
    void duplicateEmail() {
        // given

        // when
        final MemberApplicationException exception =
                MemberApplicationException.duplicateEmail("test@test.com");

        // then
        assertThat(exception.errorCode()).isEqualTo(MemberApplicationErrorCode.DUPLICATE_EMAIL);
        assertThat(exception.extensions()).isEmpty();
    }

    @Test
    @DisplayName("로그인 실패 예외는 비밀번호 정보를 노출하지 않는다")
    void loginFailed() {
        // given

        // when
        final MemberApplicationException exception = MemberApplicationException.loginFailed();

        // then
        assertThat(exception.errorCode()).isEqualTo(MemberApplicationErrorCode.LOGIN_FAILED);
        assertThat(exception.extensions()).doesNotContainKeys("email", "password");
    }
}
