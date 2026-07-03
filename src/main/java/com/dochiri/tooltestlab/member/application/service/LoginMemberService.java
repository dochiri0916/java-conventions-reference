package com.dochiri.tooltestlab.member.application.service;

import com.dochiri.tooltestlab.member.application.exception.LoginFailedException;
import com.dochiri.tooltestlab.member.application.port.in.LoginMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.LoginMemberResult;
import com.dochiri.tooltestlab.member.application.port.in.LoginMemberUseCase;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.application.port.out.PasswordEncoderPort;
import com.dochiri.tooltestlab.member.domain.model.Email;
import com.dochiri.tooltestlab.member.domain.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class LoginMemberService implements LoginMemberUseCase {

    private final MemberRepositoryPort memberRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    @Transactional(readOnly = true)
    public LoginMemberResult login(final LoginMemberCommand command) {
        final Email email = new Email(command.email());
        final Member member = memberRepositoryPort.findByEmail(email.value())
                .orElseThrow(LoginFailedException::loginFailed);

        if (!passwordEncoderPort.matches(command.password(), member.encodedPassword())) {
            throw LoginFailedException.loginFailed();
        }

        return new LoginMemberResult(member.id().value(), member.email().value(), member.name().value());
    }
}
