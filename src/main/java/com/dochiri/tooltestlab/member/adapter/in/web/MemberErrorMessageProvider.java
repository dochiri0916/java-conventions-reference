package com.dochiri.tooltestlab.member.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiErrorMessage;
import com.dochiri.tooltestlab.global.error.ApiErrorMessageProvider;
import com.dochiri.tooltestlab.member.application.exception.MemberErrorCode;
import com.dochiri.tooltestlab.member.domain.exception.MemberDomainErrorCode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MemberErrorMessageProvider implements ApiErrorMessageProvider {

    private static final Map<String, ApiErrorMessage> ERROR_MESSAGES = Map.ofEntries(
            message(MemberDomainErrorCode.INVALID_MEMBER_ID, "회원 ID 오류", "회원 ID는 빈 값일 수 없습니다."),
            message(MemberDomainErrorCode.MEMBER_ID_REQUIRED, "회원 ID 누락", "회원 ID는 필수입니다."),
            message(MemberDomainErrorCode.INVALID_EMAIL, "이메일 형식 오류", "이메일 형식이 올바르지 않습니다."),
            message(MemberDomainErrorCode.EMAIL_REQUIRED, "이메일 누락", "이메일은 필수입니다."),
            message(MemberDomainErrorCode.INVALID_PASSWORD, "비밀번호 형식 오류", "비밀번호는 빈 값일 수 없습니다."),
            message(MemberDomainErrorCode.PASSWORD_REQUIRED, "비밀번호 누락", "비밀번호는 필수입니다."),
            message(MemberDomainErrorCode.INVALID_MBR_NAME, "회원 이름 오류", "회원 이름은 빈 값일 수 없습니다."),
            message(MemberDomainErrorCode.MEMBER_NAME_REQUIRED, "회원 이름 누락", "회원 이름은 필수입니다."),
            message(MemberDomainErrorCode.INVALID_MEMBER, "회원 정보 오류", "회원 정보가 올바르지 않습니다."),
            message(MemberDomainErrorCode.MEMBER_REQUIRED, "회원 정보 누락", "회원 정보는 필수입니다."),
            message(MemberErrorCode.NAME_REQUIRED, "이름 누락", "이름은 필수입니다."),
            message(MemberErrorCode.MEMBER_NOT_FOUND, "회원 없음", "회원을 찾을 수 없습니다."),
            message(MemberErrorCode.LOGIN_FAILED, "로그인 실패", "이메일 또는 비밀번호가 올바르지 않습니다."),
            message(MemberErrorCode.DUPLICATE_EMAIL, "이메일 중복", "이미 사용 중인 이메일입니다."),
            message(MemberErrorCode.INVALID_INPUT, "입력값 오류", "입력값이 올바르지 않습니다.")
    );

    @Override
    public Map<String, ApiErrorMessage> errorMessages() {
        return Map.copyOf(ERROR_MESSAGES);
    }

    private static Map.Entry<String, ApiErrorMessage> message(
            final Enum<?> errorCode,
            final String title,
            final String detail) {
        return Map.entry(ApiErrorCode.from(errorCode), new ApiErrorMessage(title, detail));
    }
}
