package com.dochiri.tooltestlab.global.error;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiErrorMessageCatalog {

    private static final ApiErrorMessage INTERNAL_ERROR = new ApiErrorMessage(
            "서버 오류",
            "예상하지 못한 오류가 발생했습니다."
    );
    private static final ApiErrorMessage VALIDATION_ERROR = new ApiErrorMessage(
            "요청 검증 실패",
            "요청 값이 올바르지 않습니다."
    );
    private static final ApiErrorMessage DEFAULT_ERROR = new ApiErrorMessage(
            "요청 처리 실패",
            "요청을 처리할 수 없습니다."
    );
    private static final String DEFAULT_FIELD_ERROR_MESSAGE = "잘못된 값입니다.";

    private final List<ApiErrorMessageProvider> providers;

    public ApiErrorMessage internalError() {
        return INTERNAL_ERROR;
    }

    public ApiErrorMessage validationError() {
        return VALIDATION_ERROR;
    }

    public String defaultFieldErrorMessage() {
        return DEFAULT_FIELD_ERROR_MESSAGE;
    }

    public ApiErrorMessage findByCode(final String errorCode) {
        for (final ApiErrorMessageProvider provider : providers) {
            final var message = provider.findByCode(errorCode);
            if (message.isPresent()) {
                return message.orElseThrow();
            }
        }
        return DEFAULT_ERROR;
    }
}
