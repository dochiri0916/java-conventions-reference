package com.dochiri.tooltestlab.member.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberExceptionTest {

    @Test
    @DisplayName("회원 도메인 예외는 ErrorCode와 안전한 상세 정보를 가진다")
    void memberDomainException() {
        // given

        // when
        final MemberDomainException exception = MemberDomainException.invalidEmail("invalid-email");

        // then
        assertThat(exception.errorCode()).isEqualTo(MemberDomainErrorCode.EMAIL_INVALID);
        assertThat(exception.extensions()).isEmpty();
        assertThat(exception.getMessage()).isEqualTo(MemberDomainErrorCode.EMAIL_INVALID.detail());
    }
}
