package com.dochiri.tooltestlab.member.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberExceptionTest {

    @Test
    @DisplayName("InvalidEmailException 정상 생성")
    void invalidEmail() {
        // given
        var expectedCode = MemberDomainErrorCode.INVALID_EMAIL;

        // when
        var ex = InvalidEmailException.invalidEmail();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidMemberIdException 정상 생성")
    void invalidMemberId() {
        // given
        var expectedCode = MemberDomainErrorCode.INVALID_MEMBER_ID;

        // when
        var ex = InvalidMemberIdException.invalidMemberId();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidMemberNameException 정상 생성")
    void invalidMbrName() {
        // given
        var expectedCode = MemberDomainErrorCode.INVALID_MBR_NAME;

        // when
        var ex = InvalidMemberNameException.invalidMbrName();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidPasswordException 정상 생성")
    void invalidPassword() {
        // given
        var expectedCode = MemberDomainErrorCode.INVALID_PASSWORD;

        // when
        var ex = InvalidPasswordException.invalidPassword();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("MemberDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = MemberDomainErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
