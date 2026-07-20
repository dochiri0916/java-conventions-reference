package com.dochiri.tooltestlab.member.application.exception;

import com.dochiri.tooltestlab.global.exception.ApplicationException;

import java.util.Map;

public final class MemberApplicationException extends ApplicationException {

    private MemberApplicationException(
            final MemberApplicationErrorCode errorCode,
            final String detail,
            final Map<String, ?> extensions
    ) {
        super(errorCode, detail, extensions);
    }

    public static MemberApplicationException emailRequired() {
        return of(MemberApplicationErrorCode.EMAIL_REQUIRED);
    }

    public static MemberApplicationException passwordRequired() {
        return of(MemberApplicationErrorCode.PASSWORD_REQUIRED);
    }

    public static MemberApplicationException nameRequired() {
        return of(MemberApplicationErrorCode.NAME_REQUIRED);
    }

    public static MemberApplicationException duplicateEmail(final String email) {
        return new MemberApplicationException(
                MemberApplicationErrorCode.DUPLICATE_EMAIL,
                MemberApplicationErrorCode.DUPLICATE_EMAIL.detail(),
                Map.of()
        );
    }

    public static MemberApplicationException loginFailed() {
        return of(MemberApplicationErrorCode.LOGIN_FAILED);
    }

    public static MemberApplicationException memberNotFound(final String memberId) {
        return new MemberApplicationException(
                MemberApplicationErrorCode.MEMBER_NOT_FOUND,
                MemberApplicationErrorCode.MEMBER_NOT_FOUND.detail(),
                Map.of("memberId", memberId)
        );
    }

    public static MemberApplicationException invalidInput(final String detail) {
        return new MemberApplicationException(MemberApplicationErrorCode.INVALID_INPUT, detail, Map.of());
    }

    private static MemberApplicationException of(final MemberApplicationErrorCode errorCode) {
        return new MemberApplicationException(errorCode, errorCode.detail(), Map.of());
    }
}
