package com.dochiri.tooltestlab.member.application.exception;

import static java.util.Objects.requireNonNull;

public final class MemberNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberErrorCode errorCode;
    private final String missingMemberIdentifier;

    private MemberNotFoundException(final MemberErrorCode errorCode, final String identifier) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.missingMemberIdentifier = requireNonNull(identifier);
    }

    public static MemberNotFoundException memberNotFound(final String identifier) {
        return new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND, identifier);
    }

    public MemberErrorCode code() {
        return errorCode;
    }

    public String identifier() {
        return missingMemberIdentifier;
    }
}
