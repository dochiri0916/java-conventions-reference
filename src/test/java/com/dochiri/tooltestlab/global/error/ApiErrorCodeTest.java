package com.dochiri.tooltestlab.global.error;

import com.dochiri.tooltestlab.ErrorCode;
import com.dochiri.tooltestlab.member.domain.exception.MemberDomainErrorCode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApiErrorCodeTest {

    @Test
    @DisplayName("유틸리티 클래스는 인스턴스화할 수 없다")
    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    void cannotInstantiate() {
        // given
        var constructor = ApiErrorCode.class.getDeclaredConstructors()[0];

        // when & then
        constructor.setAccessible(true);
        assertThatThrownBy(constructor::newInstance)
                .hasCauseInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("null errorCode를 전달하면 NullPointerException이 발생한다")
    @SuppressFBWarnings("NP_LOAD_OF_KNOWN_NULL_VALUE")
    void nullErrorCodeThrowsNpe() {
        // given
        Enum<?> nullCode = null;

        // when & then
        assertThatThrownBy(() -> ApiErrorCode.from(nullCode))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("도메인 패키지의 에러 코드는 컨텍스트 네임스페이스를 반환한다")
    void domainErrorCodeReturnsContextNamespace() {
        // given
        var errorCode = MemberDomainErrorCode.EMAIL_REQUIRED;

        // when
        var result = ApiErrorCode.from(errorCode);

        // then
        assertThat(result).isEqualTo("MEMBER.EMAIL_REQUIRED");
    }

    @Test
    @DisplayName("global.error 패키지의 에러 코드는 GLOBAL 네임스페이스를 반환한다")
    void globalErrorCodeReturnsGlobalNamespace() {
        // given
        var errorCode = GlobalErrorCode.AUTHENTICATION_REQUIRED;

        // when
        var result = ApiErrorCode.from(errorCode);

        // then
        assertThat(result).isEqualTo("GLOBAL.AUTHENTICATION_REQUIRED");
    }

    @Test
    @DisplayName("계층 구분자가 없는 패키지의 enum은 폴백 네임스페이스를 사용한다")
    void enumWithoutLayerSegmentUsesFallback() {
        // given
        var code = java.util.concurrent.TimeUnit.SECONDS;

        // when
        var result = ApiErrorCode.from(code);

        // then
        assertThat(result).isEqualTo("TIMEUNIT.SECONDS");
    }

    @Test
    @DisplayName("application 패키지 하위의 에러 코드도 컨텍스트 네임스페이스를 반환한다")
    void applicationErrorCodeReturnsContextNamespace() {
        // given
        var code = com.dochiri.tooltestlab.member.application.exception.MemberErrorCode.MEMBER_NOT_FOUND;

        // when
        var result = ApiErrorCode.from(code);

        // then
        assertThat(result).isEqualTo("MEMBER.MEMBER_NOT_FOUND");
    }

    @Test
    @DisplayName("클래스명이 ErrorCode이고 계층 구분자가 없으면 폴백 GLOBAL을 반환한다")
    void blankFallbackReturnsGlobal() {
        // given
        var code = ErrorCode.MOCK_ERROR;

        // when
        var result = ApiErrorCode.from(code);

        // then
        assertThat(result).isEqualTo("GLOBAL.MOCK_ERROR");
    }
}
