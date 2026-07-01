package com.dochiri.tooltestlab.member.application.exception;

public enum MemberErrorCode {

    LOGIN_FAILED("로그인 실패"),
    DUPLICATE_EMAIL("이메일 중복"),
    MEMBER_NOT_FOUND("회원 없음");

    private final String message;

    MemberErrorCode(final String title) {
        this.message = title;
    }

    public String title() {
        return message;
    }
}
