package com.dochiri.tooltestlab.member.domain.exception;

public enum MemberDomainErrorCode {

    INVALID_MEMBER_ID("회원 ID는 빈 값일 수 없습니다."),
    MEMBER_ID_REQUIRED("회원 ID는 null일 수 없습니다."),
    INVALID_EMAIL("이메일 형식이 올바르지 않습니다."),
    EMAIL_REQUIRED("이메일은 null일 수 없습니다."),
    INVALID_PASSWORD("비밀번호는 빈 값일 수 없습니다."),
    PASSWORD_REQUIRED("비밀번호는 null일 수 없습니다."),
    INVALID_MBR_NAME("이름은 빈 값일 수 없습니다."),
    MEMBER_NAME_REQUIRED("이름은 null일 수 없습니다."),
    INVALID_MEMBER("회원 정보가 올바르지 않습니다."),
    MEMBER_REQUIRED("회원 정보는 null일 수 없습니다.");

    private final String message;

    MemberDomainErrorCode(final String message) {
        this.message = message;
    }

    public String title() {
        return message;
    }
}
