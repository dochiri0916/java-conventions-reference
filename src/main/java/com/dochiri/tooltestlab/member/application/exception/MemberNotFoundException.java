package com.dochiri.tooltestlab.member.application.exception;

public final class MemberNotFoundException extends MemberApplicationException {

    public MemberNotFoundException() {
        super("회원을 찾을 수 없습니다.");
    }

}
