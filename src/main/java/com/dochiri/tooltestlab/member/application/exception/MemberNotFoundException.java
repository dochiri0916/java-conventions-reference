package com.dochiri.tooltestlab.member.application.exception;

public class MemberNotFoundException extends RuntimeException {

    private final Long memberId;

    public MemberNotFoundException(Long memberId) {
        super("Member not found");
        this.memberId = memberId;
    }

    public Long memberId() {
        return memberId;
    }

}
