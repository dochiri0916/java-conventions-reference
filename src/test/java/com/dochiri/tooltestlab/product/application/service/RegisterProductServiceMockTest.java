package com.dochiri.tooltestlab.product.application.service;

import com.dochiri.tooltestlab.catalog.product.application.exception.ProductApplicationErrorCode;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductApplicationException;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.application.service.GetProductService;
import com.dochiri.tooltestlab.catalog.product.application.service.RegisterProductService;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterProductServiceMockTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    private RegisterProductService registerProductService;
    private GetProductService getProductService;

    @BeforeEach
    void setUp() {
        registerProductService = new RegisterProductService(productRepositoryPort, categoryRepositoryPort);
        getProductService = new GetProductService(productRepositoryPort, categoryRepositoryPort);
    }

    @Test
    @DisplayName("registerProduct 성공 - 카테고리 없음")
    void registerProductNoCategory() {
        // given
        when(productRepositoryPort.create(any())).thenAnswer(invocation -> invocation.getArgument(0));
        var cmd = new RegisterProductCommand("상품", "상품 설명", 1000, 10, Set.of());

        // when
        var result = registerProductService.register(cmd);

        // then
        assertThat(result.publicId()).isNotNull();
    }

    @Test
    @DisplayName("registerProduct 성공 - 카테고리 있음")
    void registerProductWithCategory() {
        // given
        when(categoryRepositoryPort.findAllByIds(Set.of(new CategoryId("cat1"))))
                .thenReturn(Set.of(Category.create("전자기기")));
        when(productRepositoryPort.create(any())).thenAnswer(invocation -> invocation.getArgument(0));
        var cmd = new RegisterProductCommand("상품", "상품 설명", 1000, 10, Set.of("cat1"));

        // when
        var result = registerProductService.register(cmd);

        // then
        assertThat(result.publicId()).isNotNull();
    }

    @Test
    @DisplayName("getProduct - 존재하지 않는 상품 예외")
    void retrieveProductNotFound() {
        // given
        when(productRepositoryPort.findById(new ProductId("nonexistent"))).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> getProductService.getProduct("nonexistent"))
                .isInstanceOfSatisfying(ProductApplicationException.class, exception ->
                        assertThat(exception.errorCode())
                                .isEqualTo(ProductApplicationErrorCode.PRODUCT_NOT_FOUND)
                );
    }
}
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;
