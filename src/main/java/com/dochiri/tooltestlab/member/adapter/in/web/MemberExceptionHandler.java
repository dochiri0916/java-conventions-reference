package com.dochiri.tooltestlab.member.adapter.in.web;

import com.dochiri.tooltestlab.global.exception.ProblemDetails;
import lombok.RequiredArgsConstructor;
import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.exception.InvalidCommandException;
import com.dochiri.tooltestlab.member.application.exception.LoginFailedException;
import com.dochiri.tooltestlab.member.application.exception.MemberApplicationException;
import com.dochiri.tooltestlab.member.application.exception.MemberNotFoundException;
import com.dochiri.tooltestlab.member.domain.exception.EmailRequiredException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidEmailException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberNameException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidPasswordException;
import com.dochiri.tooltestlab.member.domain.exception.MemberDomainException;
import com.dochiri.tooltestlab.member.domain.exception.MemberIdRequiredException;
import com.dochiri.tooltestlab.member.domain.exception.MemberNameRequiredException;
import com.dochiri.tooltestlab.member.domain.exception.MemberStatusRequiredException;
import com.dochiri.tooltestlab.member.domain.exception.PasswordRequiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class MemberExceptionHandler {

    private final ProblemDetails problemDetails;

    @ExceptionHandler(MemberDomainException.class)
    public ProblemDetail handle(final MemberDomainException exception) {
        return switch (exception) {
            case EmailRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "EMAIL_REQUIRED");
            case InvalidEmailException e -> respond(e, HttpStatus.BAD_REQUEST, "EMAIL_INVALID");
            case MemberIdRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "MEMBER_ID_REQUIRED");
            case InvalidMemberIdException e -> respond(e, HttpStatus.BAD_REQUEST, "MEMBER_ID_INVALID");
            case PasswordRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "PASSWORD_REQUIRED");
            case InvalidPasswordException e -> respond(e, HttpStatus.BAD_REQUEST, "PASSWORD_INVALID");
            case MemberNameRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "MEMBER_NAME_REQUIRED");
            case InvalidMemberNameException e -> respond(e, HttpStatus.BAD_REQUEST, "MEMBER_NAME_INVALID");
            case MemberStatusRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "MEMBER_STATUS_REQUIRED");
        };
    }

    @ExceptionHandler(MemberApplicationException.class)
    public ProblemDetail handle(final MemberApplicationException exception) {
        return switch (exception) {
            case DuplicateEmailException e -> respond(e, HttpStatus.CONFLICT, "DUPLICATE_EMAIL");
            case LoginFailedException e -> respond(e, HttpStatus.FORBIDDEN, "LOGIN_FAILED");
            case MemberNotFoundException e -> respond(e, HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND");
            case InvalidCommandException e -> respond(e, HttpStatus.BAD_REQUEST, "INVALID_INPUT");
        };
    }

    private ProblemDetail respond(final RuntimeException exception, final HttpStatus status, final String code) {
        log.info("회원 예외가 발생했습니다. type={}, code={}, message={}",
                exception.getClass().getSimpleName(), code, exception.getMessage());
        return problemDetails.create(status, code, exception.getMessage());
    }

}
