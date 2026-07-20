package com.dochiri.tooltestlab.member.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class MemberDomainException extends DomainException {

    private MemberDomainException(
            final MemberDomainErrorCode errorCode,
            final Map<String, ?> extensions
    ) {
        super(errorCode, errorCode.detail(), extensions);
    }

    public static MemberDomainException memberIdRequired() {
        return new MemberDomainException(MemberDomainErrorCode.MEMBER_ID_REQUIRED, Map.of());
    }

    public static MemberDomainException invalidMemberId(final String memberId) {
        return new MemberDomainException(
                MemberDomainErrorCode.MEMBER_ID_INVALID,
                Map.of("memberId", String.valueOf(memberId))
        );
    }

    public static MemberDomainException emailRequired() {
        return new MemberDomainException(MemberDomainErrorCode.EMAIL_REQUIRED, Map.of());
    }

    public static MemberDomainException invalidEmail(final String email) {
        return new MemberDomainException(MemberDomainErrorCode.EMAIL_INVALID, Map.of());
    }

    public static MemberDomainException passwordRequired() {
        return new MemberDomainException(MemberDomainErrorCode.PASSWORD_REQUIRED, Map.of());
    }

    public static MemberDomainException invalidPassword() {
        return new MemberDomainException(MemberDomainErrorCode.PASSWORD_INVALID, Map.of());
    }

    public static MemberDomainException memberNameRequired() {
        return new MemberDomainException(MemberDomainErrorCode.MEMBER_NAME_REQUIRED, Map.of());
    }

    public static MemberDomainException invalidMemberName(final String name) {
        return new MemberDomainException(
                MemberDomainErrorCode.MEMBER_NAME_INVALID,
                Map.of("name", String.valueOf(name))
        );
    }

    public static MemberDomainException memberRequired() {
        return new MemberDomainException(MemberDomainErrorCode.MEMBER_REQUIRED, Map.of());
    }
}
