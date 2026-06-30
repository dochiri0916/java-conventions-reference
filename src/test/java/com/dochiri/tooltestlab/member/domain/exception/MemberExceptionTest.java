package com.dochiri.tooltestlab.member.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberExceptionTest {

    @Test
    @DisplayName("InvalidEmailException 정상 생성")
    void invalidEmail() {
            // given

            // when

            // then

        var ex = InvalidEmailException.invalidEmail();
        assertThat(ex.code()).isEqualTo(MemberDomainErrorCode.INVALID_EMAIL);
    }

    @Test
    @DisplayName("InvalidMemberIdException 정상 생성")
    void invalidMemberId() {
            // given

            // when

            // then

        var ex = InvalidMemberIdException.invalidMemberId();
        assertThat(ex.code()).isEqualTo(MemberDomainErrorCode.INVALID_MEMBER_ID);
    }

    @Test
    @DisplayName("InvalidMemberNameException 정상 생성")
    void invalidMbrName() {
            // given

            // when

            // then

        var ex = InvalidMemberNameException.invalidMbrName();
        assertThat(ex.code()).isEqualTo(MemberDomainErrorCode.INVALID_MBR_NAME);
    }

    @Test
    @DisplayName("InvalidPasswordException 정상 생성")
    void invalidPassword() {
            // given

            // when

            // then

        var ex = InvalidPasswordException.invalidPassword();
        assertThat(ex.code()).isEqualTo(MemberDomainErrorCode.INVALID_PASSWORD);
    }

    @Test
    @DisplayName("MemberDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
            // given

            // when

            // then

        for (var code : MemberDomainErrorCode.values()) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
