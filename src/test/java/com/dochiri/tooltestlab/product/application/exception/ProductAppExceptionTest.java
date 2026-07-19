package com.dochiri.tooltestlab.product.application.exception;

import com.dochiri.tooltestlab.catalog.product.application.exception.ProductApplicationErrorCode;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductAppExceptionTest {

    @Test
    @DisplayName("상품 조회 실패는 상품 ID를 실패 맥락으로 보존한다")
    void productNotFound() {
        // given
        final String productId = "product-1";

        // when
        final ProductApplicationException exception = ProductApplicationException.notFound(productId);

        // then
        assertThat(exception.errorCode()).isEqualTo(ProductApplicationErrorCode.PRODUCT_NOT_FOUND);
        assertThat(exception.extensions()).containsEntry("productId", productId);
    }

    @Test
    @DisplayName("중복 상품명은 상품명을 실패 맥락으로 보존한다")
    void duplicateName() {
        // given
        final String productName = "상품명";

        // when
        final ProductApplicationException exception = ProductApplicationException.duplicateName(productName);

        // then
        assertThat(exception.errorCode()).isEqualTo(ProductApplicationErrorCode.DUPLICATE_PRODUCT_NAME);
        assertThat(exception.extensions()).containsEntry("productName", productName);
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 ID 집합을 실패 맥락으로 보존한다")
    void categoriesNotFound() {
        // given
        final Set<String> categoryIds = Set.of("category-1", "category-2");

        // when
        final ProductApplicationException exception =
                ProductApplicationException.categoriesNotFound(categoryIds);

        // then
        assertThat(exception.errorCode()).isEqualTo(ProductApplicationErrorCode.CATEGORY_NOT_FOUND);
        assertThat(exception.extensions()).containsEntry("existingCategoryIds", categoryIds);
    }

    @Test
    @DisplayName("상품 Application ErrorCode는 공개 응답 정보를 모두 제공한다")
    void errorCodes() {
        // given
        final ProductApplicationErrorCode[] errorCodes = ProductApplicationErrorCode.values();

        // when & then
        assertThat(errorCodes).allSatisfy(errorCode -> {
            assertThat(errorCode.code()).isNotBlank();
            assertThat(errorCode.kind()).isNotNull();
            assertThat(errorCode.detail()).isNotBlank();
        });
    }

}
