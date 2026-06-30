package com.dochiri.tooltestlab.member.application.port.in;

/**
 * 로그인 UseCase.
 */
public interface LoginMemberUseCase {
    LoginMemberResult login(LoginMemberCommand command);
}
