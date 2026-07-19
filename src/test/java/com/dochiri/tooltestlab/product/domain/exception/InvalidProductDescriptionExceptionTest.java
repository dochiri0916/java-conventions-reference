package com.dochiri.tooltestlab.product.domain.exception;

import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDomainException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDomainErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidProductDescriptionExceptionTest {

    @Test
    @DisplayName("필수 상품 설명 예외는 전용 ErrorCode를 가진다")
    void required() {
        // given

        // when
        final var exception = ProductDomainException.productDescriptionRequired();

        // then
        assertThat(exception.errorCode())
                .isEqualTo(ProductDomainErrorCode.PRODUCT_DESCRIPTION_REQUIRED);
    }

    @Test
    @DisplayName("짧은 상품 설명 예외는 최소 길이를 실패 맥락으로 보존한다")
    void tooShort() {
        // given
        final int minimumLength = 10;

        // when
        final var exception = ProductDomainException.productDescriptionTooShort(minimumLength);

        // then
        assertThat(exception.errorCode())
                .isEqualTo(ProductDomainErrorCode.PRODUCT_DESCRIPTION_TOO_SHORT);
        assertThat(exception.extensions()).containsEntry("minimumLength", minimumLength);
    }

    @Test
    @DisplayName("긴 상품 설명 예외는 최대 길이를 실패 맥락으로 보존한다")
    void tooLong() {
        // given
        final int maximumLength = 500;

        // when
        final var exception = ProductDomainException.productDescriptionTooLong(maximumLength);

        // then
        assertThat(exception.errorCode())
                .isEqualTo(ProductDomainErrorCode.PRODUCT_DESCRIPTION_TOO_LONG);
        assertThat(exception.extensions()).containsEntry("maximumLength", maximumLength);
    }

}
