package com.dochiri.tooltestlab.member.application.port.in;

import com.dochiri.tooltestlab.member.application.command.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.result.RegisterMemberResult;

/**
 * 회원 가입 UseCase.
 */
public interface RegisterMemberUseCase {
    RegisterMemberResult register(RegisterMemberCommand command);
}
