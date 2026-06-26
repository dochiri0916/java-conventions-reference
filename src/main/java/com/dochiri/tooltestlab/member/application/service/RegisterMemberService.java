package com.dochiri.tooltestlab.member.application.service;

import com.dochiri.tooltestlab.member.application.command.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberUseCase;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.application.result.RegisterMemberResult;
import com.dochiri.tooltestlab.member.domain.model.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterMemberService implements RegisterMemberUseCase {

    private final MemberRepositoryPort memberRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public RegisterMemberService(MemberRepositoryPort memberRepositoryPort, PasswordEncoder passwordEncoder) {
        this.memberRepositoryPort = memberRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterMemberResult register(RegisterMemberCommand command) {
        if (memberRepositoryPort.existsByEmail(command.email())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        Member member = Member.create(
                command.email(),
                passwordEncoder.encode(command.password()),
                command.name());

        Member saved = memberRepositoryPort.save(member);

        return new RegisterMemberResult(saved.id().value(), saved.email(), saved.name());
    }
}
