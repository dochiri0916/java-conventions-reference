package com.dochiri.tooltestlab.member.application.service;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberResult;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberUseCase;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.model.Email;
import com.dochiri.tooltestlab.member.domain.model.Member;
import com.dochiri.tooltestlab.member.domain.model.MemberName;
import com.dochiri.tooltestlab.member.domain.model.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class RegisterMemberService implements RegisterMemberUseCase {

    private final MemberRepositoryPort memberRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegisterMemberResult register(final RegisterMemberCommand command) {
        if (memberRepositoryPort.existsByEmail(command.email())) {
            throw DuplicateEmailException.duplicateEmail(command.email());
        }

        final Member member = Member.create(
                new Email(command.email()),
                new Password(passwordEncoder.encode(command.password())),
                new MemberName(command.name()));

        final Member saved = memberRepositoryPort.save(member);

        return new RegisterMemberResult(saved.id().value(), saved.email().value(), saved.name().value());
    }
}
