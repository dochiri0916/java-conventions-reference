package com.dochiri.tooltestlab.global.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalErrorMessageProviderTest {

    @Test
    @DisplayName("인증 필요 에러 메시지는 한국어로 제공한다")
    void authenticationRequiredMessageIsKorean() {
        // given
        var provider = new GlobalErrorMessageProvider();

        // when
        var message = provider.errorMessages().get(ApiErrorCode.from(GlobalErrorCode.AUTHENTICATION_REQUIRED));

        // then
        assertThat(message).isNotNull();
        assertThat(message.title()).isEqualTo("인증 필요");
        assertThat(message.detail()).isEqualTo("로그인이 필요합니다.");
    }
}
