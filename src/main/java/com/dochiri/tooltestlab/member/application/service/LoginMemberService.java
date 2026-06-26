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

    public LoginMemberService(MemberRepositoryPort memberRepositoryPort, PasswordEncoder passwordEncoder) {
        this.memberRepositoryPort = memberRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginMemberResult login(LoginMemberCommand command) {
        Member member = memberRepositoryPort.findByEmail(command.email())
                .orElseThrow(() -> new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(command.password(), member.password())) {
            throw new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        return new LoginMemberResult(member.id().value(), member.email(), member.name());
    }
}
