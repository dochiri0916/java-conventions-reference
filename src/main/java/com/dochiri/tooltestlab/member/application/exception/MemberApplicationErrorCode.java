package com.dochiri.tooltestlab.member.application.exception;

import com.dochiri.tooltestlab.global.exception.ErrorCode;
import com.dochiri.tooltestlab.global.exception.ErrorKind;

public enum MemberApplicationErrorCode implements ErrorCode {

    LOGIN_FAILED("LOGIN_FAILED", ErrorKind.FORBIDDEN, "이메일 또는 비밀번호가 올바르지 않습니다."),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", ErrorKind.CONFLICT, "이미 사용 중인 이메일입니다."),
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", ErrorKind.NOT_FOUND, "회원을 찾을 수 없습니다."),
    INVALID_INPUT("MEMBER_INVALID_INPUT", ErrorKind.INVALID_INPUT, "회원 입력값이 올바르지 않습니다."),
    EMAIL_REQUIRED("EMAIL_REQUIRED", ErrorKind.INVALID_INPUT, "이메일은 필수입니다."),
    PASSWORD_REQUIRED("PASSWORD_REQUIRED", ErrorKind.INVALID_INPUT, "비밀번호는 필수입니다."),
    NAME_REQUIRED("NAME_REQUIRED", ErrorKind.INVALID_INPUT, "이름은 필수입니다.");

    private final String code;
    private final ErrorKind kind;
    private final String detail;

    MemberApplicationErrorCode(final String code, final ErrorKind kind, final String detail) {
        this.code = code;
        this.kind = kind;
        this.detail = detail;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public ErrorKind kind() {
        return kind;
    }

    @Override
    public String detail() {
        return detail;
    }
}
