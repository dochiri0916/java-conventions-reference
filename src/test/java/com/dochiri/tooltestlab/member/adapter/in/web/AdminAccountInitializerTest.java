package com.dochiri.tooltestlab.member.adapter.in.web;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class AdminAccountInitializerTest {

    @Mock
    private RegisterMemberUseCase registerMemberUseCase;

    @InjectMocks
    private AdminAccountInitializer initializer;

    @Test
    @DisplayName("관리자 계정이 이미 존재하면 DuplicateEmailException을 잡아 run이 정상 종료된다")
    void catchesDuplicateEmailException() {
        // given
        var captor = ArgumentCaptor.forClass(RegisterMemberCommand.class);
        doThrow(DuplicateEmailException.class).when(registerMemberUseCase).register(captor.capture());

        // when
        initializer.run();

        // then
        assertThat(captor.getValue()).isNotNull();
    }
}
