package com.dochiri.tooltestlab.member.domain.exception;

import com.dochiri.tooltestlab.global.exception.ErrorCode;
import com.dochiri.tooltestlab.global.exception.ErrorKind;

public enum MemberDomainErrorCode implements ErrorCode {

    MEMBER_ID_REQUIRED("MEMBER_ID_REQUIRED", ErrorKind.INVALID_INPUT, "회원 ID는 필수입니다."),
    MEMBER_ID_INVALID("MEMBER_ID_INVALID", ErrorKind.INVALID_INPUT, "유효하지 않은 회원 ID입니다."),
    EMAIL_REQUIRED("EMAIL_REQUIRED", ErrorKind.INVALID_INPUT, "이메일은 필수입니다."),
    EMAIL_INVALID("EMAIL_INVALID", ErrorKind.INVALID_INPUT, "유효하지 않은 이메일입니다."),
    PASSWORD_REQUIRED("PASSWORD_REQUIRED", ErrorKind.INVALID_INPUT, "비밀번호는 필수입니다."),
    PASSWORD_INVALID("PASSWORD_INVALID", ErrorKind.INVALID_INPUT, "유효하지 않은 비밀번호입니다."),
    MEMBER_NAME_REQUIRED("MEMBER_NAME_REQUIRED", ErrorKind.INVALID_INPUT, "회원 이름은 필수입니다."),
    MEMBER_NAME_INVALID("MEMBER_NAME_INVALID", ErrorKind.INVALID_INPUT, "유효하지 않은 회원 이름입니다."),
    MEMBER_REQUIRED("MEMBER_REQUIRED", ErrorKind.INVALID_INPUT, "회원 정보는 필수입니다.");

    private final String code;
    private final ErrorKind kind;
    private final String detail;

    MemberDomainErrorCode(final String code, final ErrorKind kind, final String detail) {
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
