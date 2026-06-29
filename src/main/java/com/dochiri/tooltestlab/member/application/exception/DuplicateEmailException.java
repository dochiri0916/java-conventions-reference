package com.dochiri.tooltestlab.member.application.exception;

public final class DuplicateEmailException extends MemberApplicationException {

    public DuplicateEmailException(final String email) {
        super(MemberErrorCode.DUPLICATE_EMAIL, "이미 사용 중인 이메일입니다. email=" + email);
    }
}
