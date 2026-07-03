package com.dochiri.tooltestlab.global.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalErrorCodeMappingProviderTest {

    @Test
    @DisplayName("인증 필요 예외는 401 UNAUTHORIZED로 매핑한다")
    void authenticationRequiredExceptionMapsToUnauthorized() {
        // given
        var provider = new GlobalErrorCodeMappingProvider();
        var exception = AuthenticationRequiredException.authenticationRequired();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(GlobalErrorCode.AUTHENTICATION_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(mappedError.orElseThrow().mapping().problemType()).isEqualTo(ProblemType.UNAUTHORIZED);
    }
}
