package com.dochiri.tooltestlab.member.application.exception;

public final class LoginFailedException extends MemberApplicationException {

    public LoginFailedException() {
        super(MemberErrorCode.LOGIN_FAILED, "이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    public LoginFailedException(final String message) {
        super(MemberErrorCode.LOGIN_FAILED, message);
    }
}
