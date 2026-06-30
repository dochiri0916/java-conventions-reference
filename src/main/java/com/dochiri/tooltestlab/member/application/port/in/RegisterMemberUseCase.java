package com.dochiri.tooltestlab.member.application.port.in;

/**
 * 회원 가입 UseCase.
 */
public interface RegisterMemberUseCase {
    RegisterMemberResult register(RegisterMemberCommand command);
}
