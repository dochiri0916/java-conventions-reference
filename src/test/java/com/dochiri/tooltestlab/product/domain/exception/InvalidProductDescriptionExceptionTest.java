package com.dochiri.tooltestlab.product.domain.exception;

import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDomainErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InvalidProductDescriptionExceptionTest {

    @Test
    @DisplayName("필수 상품 설명 예외는 의미 코드만 보존한다")
    void createsRequiredFailure() {
        // given
        ProductDomainErrorCode expectedCode = ProductDomainErrorCode.PRODUCT_DESCRIPTION_REQUIRED;

        // when
        InvalidProductDescriptionException exception = InvalidProductDescriptionException.required();

        // then
        assertThat(exception.code()).isEqualTo(expectedCode);
        assertThat(exception.getMessage()).isEqualTo(expectedCode.name());
    }

    @Test
    @DisplayName("짧은 상품 설명 예외는 의미 코드만 보존한다")
    void createsTooShortFailure() {
        // given
        ProductDomainErrorCode expectedCode = ProductDomainErrorCode.PRODUCT_DESCRIPTION_TOO_SHORT;

        // when
        InvalidProductDescriptionException exception = InvalidProductDescriptionException.tooShort();

        // then
        assertThat(exception.code()).isEqualTo(expectedCode);
        assertThat(exception.getMessage()).isEqualTo(expectedCode.name());
    }

    @Test
    @DisplayName("긴 상품 설명 예외는 의미 코드만 보존한다")
    void createsTooLongFailure() {
        // given
        ProductDomainErrorCode expectedCode = ProductDomainErrorCode.PRODUCT_DESCRIPTION_TOO_LONG;

        // when
        InvalidProductDescriptionException exception = InvalidProductDescriptionException.tooLong();

        // then
        assertThat(exception.code()).isEqualTo(expectedCode);
        assertThat(exception.getMessage()).isEqualTo(expectedCode.name());
    }
}
