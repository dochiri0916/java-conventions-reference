package com.dochiri.tooltestlab.member.application.service;

import com.dochiri.tooltestlab.member.application.command.LoginMemberCommand;
import com.dochiri.tooltestlab.member.application.exception.LoginFailedException;
import com.dochiri.tooltestlab.member.application.port.in.LoginMemberUseCase;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.application.result.LoginMemberResult;
import com.dochiri.tooltestlab.member.domain.model.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LoginMemberService implements LoginMemberUseCase {

    private final MemberRepositoryPort memberRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public LoginMemberService(final MemberRepositoryPort memberRepositoryPort, final PasswordEncoder passwordEncoder) {
        this.memberRepositoryPort = memberRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginMemberResult login(final LoginMemberCommand command) {
        final Member member = memberRepositoryPort.findByEmail(command.email())
                .orElseThrow(() -> new LoginFailedException());

        if (!passwordEncoder.matches(command.password(), member.password().value())) {
            throw new LoginFailedException();
        }

        return new LoginMemberResult(member.id().value(), member.email().value(), member.name().value());
    }
}
