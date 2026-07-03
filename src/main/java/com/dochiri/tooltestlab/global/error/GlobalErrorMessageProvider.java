package com.dochiri.tooltestlab.global.error;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GlobalErrorMessageProvider implements ApiErrorMessageProvider {

    private static final Map<String, ApiErrorMessage> ERROR_MESSAGES = Map.of(
            ApiErrorCode.from(GlobalErrorCode.AUTHENTICATION_REQUIRED),
            new ApiErrorMessage("인증 필요", "로그인이 필요합니다.")
    );

    @Override
    public Map<String, ApiErrorMessage> errorMessages() {
        return Map.copyOf(ERROR_MESSAGES);
    }
}
