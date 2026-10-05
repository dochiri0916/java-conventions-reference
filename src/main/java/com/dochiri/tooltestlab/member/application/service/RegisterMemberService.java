package com.dochiri.tooltestlab.member.application.service;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.in.command.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.result.RegisterMemberResult;
import com.dochiri.tooltestlab.member.application.port.in.usecase.RegisterMemberUseCase;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.application.port.out.PasswordEncoderPort;
import com.dochiri.tooltestlab.member.domain.vo.Email;
import com.dochiri.tooltestlab.member.domain.Member;
import com.dochiri.tooltestlab.member.application.MemberEventMapper;
import com.dochiri.tooltestlab.member.application.MemberResultMapper;
import com.dochiri.tooltestlab.member.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.member.domain.vo.MemberName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class RegisterMemberService implements RegisterMemberUseCase {

    private final MemberRepositoryPort memberRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public RegisterMemberResult register(final RegisterMemberCommand command) {
        final Email email = Email.of(
                command.email());
        if (memberRepositoryPort.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }

        final Member member = Member.create(
                email,
                passwordEncoderPort.encode(
                        command.password()),
                MemberName.of(
                        command.name()));

        final Member saved = memberRepositoryPort.create(member);
        integrationEventPort.publish(
                MemberEventMapper.toIntegration(
                        saved.registeredEvent()));

        return MemberResultMapper.toRegisterResult(saved);
    }
}
