package com.dochiri.tooltestlab.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public final class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(final BusinessException exception) {
        final ErrorCode errorCode = exception.errorCode();
        final ProblemDetail problemDetail = toProblemDetail(errorCode);

        exception.extensions().forEach(problemDetail::setProperty);

        return problemDetail;
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLockingFailure(final ObjectOptimisticLockingFailureException exception) {
        return toProblemDetail(GlobalErrorCode.CONCURRENT_MODIFICATION);
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

        return toProblemDetail(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    private static ProblemDetail toProblemDetail(final ErrorCode errorCode) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                statusOf(errorCode.kind()),
                errorCode.detail()
        );
        problemDetail.setProperty("code", errorCode.code());

        return problemDetail;
    }

    private static HttpStatus statusOf(final ErrorKind kind) {
        return switch (kind) {
            case INVALID_INPUT -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT, INVALID_STATE -> HttpStatus.CONFLICT;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

}
