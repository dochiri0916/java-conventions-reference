package com.dochiri.tooltestlab.member.application.service;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberResult;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberUseCase;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.application.port.out.PasswordEncoderPort;
import com.dochiri.tooltestlab.member.domain.vo.Email;
import com.dochiri.tooltestlab.member.domain.Member;
import com.dochiri.tooltestlab.member.domain.vo.MemberName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class RegisterMemberService implements RegisterMemberUseCase {

    private final MemberRepositoryPort memberRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    @Transactional
    public RegisterMemberResult register(final RegisterMemberCommand command) {
        final Email email = Email.of(command.email());
        if (memberRepositoryPort.existsByEmail(email.value())) {
            throw new DuplicateEmailException();
        }

        final Member member = Member.create(
                email,
                passwordEncoderPort.encode(command.password()),
                MemberName.of(command.name()));

        final Member saved = memberRepositoryPort.create(member);

        return new RegisterMemberResult(saved.id().value(), saved.email().value(), saved.name().value());
    }
}
