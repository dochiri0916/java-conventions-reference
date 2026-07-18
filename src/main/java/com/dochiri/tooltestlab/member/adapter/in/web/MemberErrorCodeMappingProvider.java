package com.dochiri.tooltestlab.member.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ErrorCodeMappingProvider;
import com.dochiri.tooltestlab.global.error.ProblemType;
import com.dochiri.tooltestlab.member.application.exception.*;
import com.dochiri.tooltestlab.member.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class MemberErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    private static final Map<String, ApiExceptionMapper.Mapping> ERROR_CODE_MAPPINGS = Map.ofEntries(
            validation(MemberDomainErrorCode.INVALID_MEMBER_ID),
            validation(MemberDomainErrorCode.MEMBER_ID_REQUIRED),
            validation(MemberDomainErrorCode.INVALID_EMAIL),
            validation(MemberDomainErrorCode.EMAIL_REQUIRED),
            validation(MemberDomainErrorCode.INVALID_PASSWORD),
            validation(MemberDomainErrorCode.PASSWORD_REQUIRED),
            validation(MemberDomainErrorCode.INVALID_MBR_NAME),
            validation(MemberDomainErrorCode.MEMBER_NAME_REQUIRED),
            validation(MemberDomainErrorCode.INVALID_MEMBER),
            validation(MemberDomainErrorCode.MEMBER_REQUIRED),
            validation(MemberErrorCode.NAME_REQUIRED),
            notFound(),
            unauthorized(),
            conflict(),
            validation(MemberErrorCode.INVALID_INPUT)
    );

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.copyOf(ERROR_CODE_MAPPINGS);
    }

    @Override
    public Optional<ApiExceptionMapper.MappedError> resolve(final RuntimeException exception) {
        if (exception instanceof InvalidMemberIdException invalidMemberIdException) {
            return resolve(invalidMemberIdException.code());
        }
        if (exception instanceof InvalidEmailException invalidEmailException) {
            return resolve(invalidEmailException.code());
        }
        if (exception instanceof InvalidPasswordException invalidPasswordException) {
            return resolve(invalidPasswordException.code());
        }
        if (exception instanceof InvalidMemberNameException invalidMemberNameException) {
            return resolve(invalidMemberNameException.code());
        }
        if (exception instanceof InvalidMemberException invalidMemberException) {
            return resolve(invalidMemberException.code());
        }
        if (exception instanceof DuplicateEmailException duplicateEmailException) {
            return resolve(duplicateEmailException.code());
        }
        if (exception instanceof MemberNotFoundException memberNotFoundException) {
            return resolve(memberNotFoundException.code());
        }
        if (exception instanceof LoginFailedException loginFailedException) {
            return resolve(loginFailedException.code());
        }
        if (exception instanceof InvalidCommandException invalidCommandException) {
            return resolve(invalidCommandException.code());
        }
        return Optional.empty();
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> validation(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> notFound() {
        return mapping(MemberErrorCode.MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> unauthorized() {
        return mapping(MemberErrorCode.LOGIN_FAILED, HttpStatus.UNAUTHORIZED, ProblemType.UNAUTHORIZED);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> conflict() {
        return mapping(MemberErrorCode.DUPLICATE_EMAIL, HttpStatus.CONFLICT, ProblemType.CONFLICT);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> mapping(
            final Enum<?> errorCode,
            final HttpStatus status,
            final ProblemType problemType
    ) {
        return Map.entry(ApiErrorCode.from(errorCode), new ApiExceptionMapper.Mapping(status, problemType));
    }
}
