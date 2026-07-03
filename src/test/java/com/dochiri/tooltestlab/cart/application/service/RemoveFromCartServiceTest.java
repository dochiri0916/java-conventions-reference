package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.exception.CartNotFoundException;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartCommand;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoveFromCartServiceTest {

    @Mock
    private CartRepositoryPort cartRepositoryPort;

    private RemoveFromCartService removeFromCartService;

    @BeforeEach
    void setUp() {
        removeFromCartService = new RemoveFromCartService(cartRepositoryPort);
    }

    @Test
    @DisplayName("장바구니를 찾을 수 없으면 CartNotFoundException을 던진다")
    void cartNotFoundThrowsException() {
        // given
        var command = new RemoveFromCartCommand("member1", "product1");
        when(cartRepositoryPort.findByMemberId(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> removeFromCartService.remove(command))
                .isInstanceOf(CartNotFoundException.class);
    }
}
