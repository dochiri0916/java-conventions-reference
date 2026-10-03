package com.dochiri.tooltestlab.member.adapter.in.bootstrap;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class AdminAccountInitializer implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AdminAccountInitializer.class);

    private final RegisterMemberUseCase registerMemberUseCase;

    @Override
    public void run(final String... args) {
        try {
            registerMemberUseCase.register(new RegisterMemberCommand("admin@test.com", "1234", "관리자"));
            LOG.info("[AdminAccountInitializer] 관리자 계정 생성 완료");
        } catch (DuplicateEmailException exception) {
            LOG.info("[AdminAccountInitializer] 관리자 계정이 이미 존재합니다: {}", exception.getMessage());
        }
    }
}
