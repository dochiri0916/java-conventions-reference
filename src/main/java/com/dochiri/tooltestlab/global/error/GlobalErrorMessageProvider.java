package com.dochiri.tooltestlab.global.error;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GlobalErrorMessageProvider implements ApiErrorMessageProvider {

    private static final Map<String, ApiErrorMessage> ERROR_MESSAGES = Map.ofEntries(
            message(GlobalErrorCode.INTERNAL_SERVER_ERROR, "서버 오류", "예상하지 못한 오류가 발생했습니다."),
            message(GlobalErrorCode.VALIDATION_ERROR, "요청 검증 실패", "요청 값이 올바르지 않습니다."),
            message(GlobalErrorCode.AUTHENTICATION_REQUIRED, "인증 필요", "로그인이 필요합니다."),
            message(GlobalErrorCode.MALFORMED_REQUEST_BODY, "잘못된 요청 본문", "요청 본문 형식이 올바르지 않습니다."),
            message(GlobalErrorCode.PARAMETER_TYPE_MISMATCH, "요청 값 오류", "요청 값의 형식이 올바르지 않습니다."),
            message(GlobalErrorCode.MISSING_PARAMETER, "필수 요청값 누락", "필수 요청값이 누락되었습니다."),
            message(GlobalErrorCode.METHOD_NOT_ALLOWED, "지원하지 않는 메서드", "요청 메서드를 지원하지 않습니다."),
            message(GlobalErrorCode.ROUTE_NOT_FOUND, "요청 경로 없음", "요청한 경로를 찾을 수 없습니다."),
            message(GlobalErrorCode.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입", "요청 콘텐츠 형식을 지원하지 않습니다."),
            message(GlobalErrorCode.NOT_ACCEPTABLE, "지원하지 않는 응답 형식", "요청한 응답 형식을 지원하지 않습니다.")
    );

    @Override
    public Map<String, ApiErrorMessage> errorMessages() {
        return Map.copyOf(ERROR_MESSAGES);
    }

    private static Map.Entry<String, ApiErrorMessage> message(
            final GlobalErrorCode errorCode,
            final String title,
            final String detail) {
        return Map.entry(ApiErrorCode.from(errorCode), new ApiErrorMessage(title, detail));
    }
}
