package com.dochiri.tooltestlab.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
public final class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_ERROR_TITLE = "서버 오류";
    private static final String INTERNAL_ERROR_DETAIL = "일시적인 오류가 발생했습니다.";

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(final BusinessException exception) {
        final ErrorCode errorCode = exception.errorCode();
        final HttpStatus status = statusOf(errorCode.kind());

        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());

        problemDetail.setTitle(titleOf(errorCode.kind()));
        problemDetail.setType(URI.create("/problems/" + toType(errorCode.code())));
        problemDetail.setProperty("code", errorCode.code());

        exception.extensions().forEach(problemDetail::setProperty);

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnhandledException(
            final Exception exception,
            final HttpServletRequest request
    ) {
        log.error(
                "미처리 예외가 발생했습니다. uri={}, method={}",
                request.getRequestURI(),
                request.getMethod(),
                exception
        );

        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                INTERNAL_ERROR_DETAIL
        );
        problemDetail.setTitle(INTERNAL_ERROR_TITLE);

        return problemDetail;
    }

    private static HttpStatus statusOf(final ErrorKind kind) {
        return switch (kind) {
            case INVALID_INPUT -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT, INVALID_STATE -> HttpStatus.CONFLICT;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
        };
    }

    private static String titleOf(final ErrorKind kind) {
        return switch (kind) {
            case INVALID_INPUT -> "잘못된 요청";
            case NOT_FOUND -> "리소스를 찾을 수 없음";
            case CONFLICT -> "요청 충돌";
            case FORBIDDEN -> "접근 거부";
            case INVALID_STATE -> "잘못된 상태";
        };
    }

    private static String toType(final String code) {
        return code.toLowerCase(Locale.ROOT)
                .replace('_', '-');
    }

}
