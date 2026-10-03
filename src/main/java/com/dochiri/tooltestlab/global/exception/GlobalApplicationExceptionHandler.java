package com.dochiri.tooltestlab.global.exception;

import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class GlobalApplicationExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalApplicationExceptionHandler.class);

    private final ProblemDetails problemDetails;

    public GlobalApplicationExceptionHandler(final ProblemDetails problemDetails) {
        this.problemDetails = problemDetails;
    }

    @ExceptionHandler(RepositoryOperationException.class)
    public ProblemDetail handleRepositoryOperation(final RepositoryOperationException exception,
                                                 final HttpServletRequest request) {
        log.error("저장소 작업 예외가 발생했습니다. uri={}, method={}",
                request.getRequestURI(), request.getMethod(), exception);
        return problemDetails.create(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "일시적인 오류가 발생했습니다.");
    }

}
