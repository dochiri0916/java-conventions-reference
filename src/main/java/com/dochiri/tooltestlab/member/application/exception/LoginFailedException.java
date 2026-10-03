package com.dochiri.tooltestlab.member.application.exception;

public final class LoginFailedException extends MemberApplicationException {

    public LoginFailedException() {
        super("이메일 또는 비밀번호가 올바르지 않습니다.");
    }

}
