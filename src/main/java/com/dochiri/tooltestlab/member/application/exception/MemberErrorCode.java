package com.dochiri.tooltestlab.member.application.exception;

public enum MemberErrorCode {

    LOGIN_FAILED("로그인 실패"),
    DUPLICATE_EMAIL("이메일 중복"),
    MEMBER_NOT_FOUND("회원 없음"),
    INVALID_INPUT("입력값이 올바르지 않습니다."),
    EMAIL_REQUIRED("이메일은 필수입니다."),
    PASSWORD_REQUIRED("비밀번호는 필수입니다."),
    NAME_REQUIRED("이름은 필수입니다."),
    AUTHENTICATION_REQUIRED("인증이 필요합니다.");

    private final String message;

    MemberErrorCode(final String title) {
        this.message = title;
    }

    public String title() {
        return message;
    }
}
