package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.port.in.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
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
class AddToCartServiceTest {

    @Mock
    private CartRepositoryPort cartRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    private AddToCartService addToCartService;

    @BeforeEach
    void setUp() {
        addToCartService = new AddToCartService(cartRepositoryPort, productRepositoryPort);
    }

    @Test
    @DisplayName("상품을 찾을 수 없으면 ProductNotFoundException을 던진다")
    void productNotFoundThrowsException() {
        // given
        var command = new AddToCartCommand("member1", "product1", 2);
        when(productRepositoryPort.findById(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> addToCartService.add(command))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
