package com.dochiri.tooltestlab.member.application.port.in.usecase;

import com.dochiri.tooltestlab.member.application.port.in.command.LoginMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.result.LoginMemberResult;

/**
 * 로그인 UseCase.
 */
@FunctionalInterface
public interface LoginMemberUseCase {
    LoginMemberResult login(LoginMemberCommand command);
}
