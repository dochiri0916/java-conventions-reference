package com.dochiri.tooltestlab.member.application.exception;

public final class MemberNotFoundException extends MemberApplicationException {

    public MemberNotFoundException(final String identifier) {
        super(MemberErrorCode.MEMBER_NOT_FOUND, "회원을 찾을 수 없습니다. identifier=" + identifier);
    }
}
