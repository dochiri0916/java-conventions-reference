package com.dochiri.tooltestlab.member.application.port.in.usecase;

import com.dochiri.tooltestlab.member.application.port.in.command.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.result.RegisterMemberResult;

/**
 * 회원 가입 UseCase.
 */
@FunctionalInterface
public interface RegisterMemberUseCase {
    RegisterMemberResult register(RegisterMemberCommand command);
}
