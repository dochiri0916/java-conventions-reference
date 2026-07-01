package com.dochiri.tooltestlab.member.adapter.in.web;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 관리자 계정을 생성한다. 비밀번호 인코딩이 필요하므로 data.sql 대신 Java로 처리한다.
 * 테스트 프로파일에서는 data.sql 시드 데이터만으로 충분하므로 실행하지 않는다.
 */
@Component
@RequiredArgsConstructor
@Profile("!test")
public class AdminAccountInitializer implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AdminAccountInitializer.class);

    private final RegisterMemberUseCase registerMemberUseCase;

    @Override
    public void run(String... args) {
        try {
            registerMemberUseCase.register(new RegisterMemberCommand("admin@test.com", "1234", "관리자"));
            LOG.info("[AdminAccountInitializer] 관리자 계정 생성 완료");
        } catch (DuplicateEmailException e) {
            LOG.info("[AdminAccountInitializer] 관리자 계정이 이미 존재합니다: {}", e.getMessage());
        }
    }
}
