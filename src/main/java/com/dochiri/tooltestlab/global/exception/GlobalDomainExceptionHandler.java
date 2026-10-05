package com.dochiri.tooltestlab.global.exception;

import com.dochiri.tooltestlab.global.domain.exception.InvalidOutboxAttemptsException;
import com.dochiri.tooltestlab.global.domain.exception.InvalidOutboxSequenceException;
import com.dochiri.tooltestlab.global.domain.exception.InvalidQuantityException;
import com.dochiri.tooltestlab.global.domain.exception.MoneyRequiredException;
import com.dochiri.tooltestlab.global.domain.exception.MoneyOverflowException;
import com.dochiri.tooltestlab.global.domain.exception.NegativeMoneyException;
import com.dochiri.tooltestlab.global.domain.exception.NegativeMoneyMultiplierException;
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
public final class GlobalDomainExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalDomainExceptionHandler.class);

    private final ProblemDetails problemDetails;

    public GlobalDomainExceptionHandler(final ProblemDetails problemDetails) {
        this.problemDetails = problemDetails;
    }

    @ExceptionHandler(NegativeMoneyException.class)
    public ProblemDetail handleNegativeMoney(final NegativeMoneyException exception) {
        return respond(exception, HttpStatus.BAD_REQUEST, "MONEY_NEGATIVE");
    }

    @ExceptionHandler(MoneyOverflowException.class)
    public ProblemDetail handleMoneyOverflow(final MoneyOverflowException exception) {
        return respond(exception, HttpStatus.CONFLICT, "MONEY_OVERFLOW");
    }

    @ExceptionHandler(NegativeMoneyMultiplierException.class)
    public ProblemDetail handleNegativeMoneyMultiplier(final NegativeMoneyMultiplierException exception) {
        return respond(exception, HttpStatus.CONFLICT, "MONEY_MULTIPLIER_NEGATIVE");
    }

    @ExceptionHandler(MoneyRequiredException.class)
    public ProblemDetail handleMoneyRequired(final MoneyRequiredException exception) {
        return respond(exception, HttpStatus.BAD_REQUEST, "MONEY_REQUIRED");
    }

    @ExceptionHandler(InvalidOutboxSequenceException.class)
    public ProblemDetail handleInvalidOutboxSequence(final InvalidOutboxSequenceException exception) {
        return respond(exception, HttpStatus.INTERNAL_SERVER_ERROR, "OUTBOX_SEQUENCE_INVALID");
    }

    @ExceptionHandler(InvalidOutboxAttemptsException.class)
    public ProblemDetail handleInvalidOutboxAttempts(final InvalidOutboxAttemptsException exception) {
        return respond(exception, HttpStatus.INTERNAL_SERVER_ERROR, "OUTBOX_ATTEMPTS_INVALID");
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ProblemDetail handleInvalidQuantity(final InvalidQuantityException exception) {
        return respond(exception, HttpStatus.BAD_REQUEST, "QUANTITY_INVALID");
    }

    private ProblemDetail respond(final RuntimeException exception, final HttpStatus status, final String code) {
        log.info("도메인 예외가 발생했습니다. type={}, code={}, message={}",
                exception.getClass().getSimpleName(), code, exception.getMessage());
        return problemDetails.create(status, code, exception.getMessage());
    }

}
