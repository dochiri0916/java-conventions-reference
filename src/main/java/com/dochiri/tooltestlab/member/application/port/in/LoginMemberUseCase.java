package com.dochiri.tooltestlab.member.application.port.in;

import com.dochiri.tooltestlab.member.application.command.LoginMemberCommand;
import com.dochiri.tooltestlab.member.application.result.LoginMemberResult;

/**
 * 로그인 UseCase.
 */
public interface LoginMemberUseCase {
    LoginMemberResult login(LoginMemberCommand command);
}
