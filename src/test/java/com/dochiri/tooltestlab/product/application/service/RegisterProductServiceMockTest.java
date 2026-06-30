package com.dochiri.tooltestlab.product.application.service;

import com.dochiri.tooltestlab.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.product.application.port.in.RegisterProductCommand;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import com.dochiri.tooltestlab.shared.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterProductServiceMockTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    private RegisterProductService registerProductService;
    private GetProductService getProductService;

    @BeforeEach
    void setUp() {
        registerProductService = new RegisterProductService(productRepositoryPort);
        getProductService = new GetProductService(productRepositoryPort);
    }

    @Test
    @DisplayName("registerProduct 성공 - 카테고리 없음")
    void registerProductNoCategory() {
            // given

            // when

            // then

        when(productRepositoryPort.save(any(), any())).thenAnswer(inv -> inv.getArgument(0));
        var cmd = new RegisterProductCommand("상품", "설명", 1000, 10, List.of());
        var result = registerProductService.register(cmd);
        assertThat(result.publicId()).isNotNull();
    }

    @Test
    @DisplayName("registerProduct 성공 - 카테고리 있음")
    void registerProductWithCategory() {
            // given

            // when

            // then

        when(productRepositoryPort.findCategoriesByPublicIds(any())).thenReturn(List.of());
        when(productRepositoryPort.save(any(), any())).thenAnswer(inv -> inv.getArgument(0));
        var cmd = new RegisterProductCommand("상품", "설명", 1000, 10, List.of("cat1"));
        var result = registerProductService.register(cmd);
        assertThat(result.publicId()).isNotNull();
    }

    @Test
    @DisplayName("getProduct - 존재하지 않는 상품 예외")
    void getProductNotFound() {
            // given

            // when

            // then

        when(productRepositoryPort.findByPublicId("nonexistent")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> getProductService.getProduct("nonexistent"))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
