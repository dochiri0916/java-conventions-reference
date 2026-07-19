package com.dochiri.tooltestlab.product.domain.exception;

import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDomainException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDomainErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDomainExceptionTest {

    @Test
    @DisplayName("유효하지 않은 상품 ID는 입력값을 실패 맥락으로 보존한다")
    void invalidProductId() {
        // given
        final String productId = "invalid-id";

        // when
        final var exception = ProductDomainException.invalidProductId(productId);

        // then
        assertThat(exception.errorCode()).isEqualTo(ProductDomainErrorCode.PRODUCT_ID_INVALID);
        assertThat(exception.extensions()).containsEntry("productId", productId);
    }

    @Test
    @DisplayName("재고 부족은 현재 재고와 요청 수량을 실패 맥락으로 보존한다")
    void insufficientStock() {
        // given
        final int currentStock = 5;
        final int requestedQuantity = 10;

        // when
        final var exception = ProductDomainException.insufficientStock(currentStock, requestedQuantity);

        // then
        assertThat(exception.errorCode()).isEqualTo(ProductDomainErrorCode.STOCK_INSUFFICIENT);
        assertThat(exception.extensions())
                .containsEntry("currentStock", currentStock)
                .containsEntry("requestedQuantity", requestedQuantity);
    }

    @Test
    @DisplayName("상품 Domain ErrorCode는 공개 응답 정보를 모두 제공한다")
    void errorCodes() {
        // given
        final ProductDomainErrorCode[] errorCodes = ProductDomainErrorCode.values();

        // when & then
        assertThat(errorCodes).allSatisfy(errorCode -> {
            assertThat(errorCode.code()).isNotBlank();
            assertThat(errorCode.kind()).isNotNull();
            assertThat(errorCode.detail()).isNotBlank();
        });
    }

}
