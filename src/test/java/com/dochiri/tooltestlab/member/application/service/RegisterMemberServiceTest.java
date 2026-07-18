package com.dochiri.tooltestlab.member.application.service;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.application.port.out.PasswordEncoderPort;
import com.dochiri.tooltestlab.member.domain.model.EncodedPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RegisterMemberServiceTest {

    @Test
    @DisplayName("회원 가입은 정규화된 이메일로 중복을 검사한다")
    void registerChecksDuplicateWithNormalizedEmail() {
        // given
        var memberRepositoryPort = mock(MemberRepositoryPort.class);
        var passwordEncoderPort = mock(PasswordEncoderPort.class);
        var service = new RegisterMemberService(memberRepositoryPort, passwordEncoderPort);
        var command = new RegisterMemberCommand(" user@example.com ", "password123", "홍길동");
        when(memberRepositoryPort.existsByEmail("user@example.com")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> service.register(command))
                .isInstanceOf(DuplicateEmailException.class);
        verify(memberRepositoryPort).existsByEmail("user@example.com");
    }

    @Test
    @DisplayName("회원 가입은 정규화된 이메일을 저장 결과로 반환한다")
    void registerReturnsNormalizedEmail() {
        // given
        var memberRepositoryPort = mock(MemberRepositoryPort.class);
        var passwordEncoderPort = mock(PasswordEncoderPort.class);
        var service = new RegisterMemberService(memberRepositoryPort, passwordEncoderPort);
        var command = new RegisterMemberCommand(" user@example.com ", "password123", "홍길동");
        when(memberRepositoryPort.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoderPort.encode("password123")).thenReturn(new EncodedPassword("encoded"));
        when(memberRepositoryPort.save(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        var result = service.register(command);

        // then
        org.assertj.core.api.Assertions.assertThat(result.email()).isEqualTo("user@example.com");
    }
}
