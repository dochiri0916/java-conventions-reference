package com.dochiri.tooltestlab.product.adapter.in.web;

import com.dochiri.tooltestlab.catalog.product.application.exception.ProductApplicationErrorCode;
import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ProblemType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProductApplicationErrorCodeMappingProviderTest {

    @Test
    @DisplayName("상품 에러 코드 16개를 모두 매핑한다")
    void hasAllMappings() {
        // given
        var provider = new ProductErrorCodeMappingProvider();

        // when
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // then
        assertThat(mappings).isNotNull().hasSize(16);
    }

    @Test
    @DisplayName("모든 매핑이 non-null status와 problemType을 가진다")
    void allMappingsHaveValidStatusAndProblemType() {
        // given
        var provider = new ProductErrorCodeMappingProvider();
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // when & then
        assertThat(mappings.values()).allSatisfy(mapping -> {
            assertThat(mapping.status()).isNotNull();
            assertThat(mapping.problemType()).isNotNull();
        });
    }

    @Test
    @DisplayName("LOW_STOCK은 409 BUSINESS_RULE_VIOLATION으로 매핑한다")
    void lowStock() {
        // given
        var provider = new ProductErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(ProductDomainErrorCode.LOW_STOCK));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.BUSINESS_RULE_VIOLATION);
    }

    @Test
    @DisplayName("PRODUCT_NOT_FOUND는 404 NOT_FOUND로 매핑한다")
    void productNotFound() {
        // given
        var provider = new ProductErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(ProductApplicationErrorCode.PRODUCT_NOT_FOUND));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.NOT_FOUND);
    }

    @Test
    @DisplayName("PRODUCT_ID_REQUIRED는 400 VALIDATION_FAILED로 매핑한다")
    void productIdRequired() {
        // given
        var provider = new ProductErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(ProductDomainErrorCode.PRODUCT_ID_REQUIRED));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("MONEY_NEGATIVE는 400 VALIDATION_FAILED로 매핑한다")
    void moneyNegative() {
        // given
        var provider = new ProductErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(ProductDomainErrorCode.MONEY_NEGATIVE));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("InvalidProductIdException을 타입 기반으로 매핑한다")
    void resolveInvalidProductIdException() {
        // given
        var provider = new ProductErrorCodeMappingProvider();
        var exception = InvalidProductIdException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ProductDomainErrorCode.PRODUCT_ID_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidProductNameException을 타입 기반으로 매핑한다")
    void resolveInvalidProductNameException() {
        // given
        var provider = new ProductErrorCodeMappingProvider();
        var exception = InvalidProductNameException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ProductDomainErrorCode.PRODUCT_NAME_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidStockException을 타입 기반으로 매핑한다")
    void resolveInvalidStockException() {
        // given
        var provider = new ProductErrorCodeMappingProvider();
        var exception = InvalidStockException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ProductDomainErrorCode.STOCK_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidProductException을 타입 기반으로 매핑한다")
    void resolveInvalidProductException() {
        // given
        var provider = new ProductErrorCodeMappingProvider();
        var exception = InvalidProductException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ProductDomainErrorCode.PRODUCT_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidMoneyException을 타입 기반으로 매핑한다")
    void resolveInvalidMoneyException() {
        // given
        var provider = new ProductErrorCodeMappingProvider();
        var exception = InvalidMoneyException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ProductDomainErrorCode.MONEY_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidMaxCountException을 타입 기반으로 매핑한다")
    void resolveInvalidMaxCountException() {
        // given
        var provider = new ProductErrorCodeMappingProvider();
        var exception = InvalidMaxCountException.notPositive(0);

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ProductDomainErrorCode.MAX_COUNT_NOT_POSITIVE));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InsufficientStockException을 타입 기반으로 매핑한다")
    void resolveInsufficientStockException() {
        // given
        var provider = new ProductErrorCodeMappingProvider();
        var exception = InsufficientStockException.lowStock(3, 5);

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(ProductDomainErrorCode.LOW_STOCK));
    }

    @Test
    @DisplayName("ProductNotFoundException을 타입 기반으로 매핑한다")
    void resolveProductNotFoundException() {
        // given
        var provider = new ProductErrorCodeMappingProvider();
        var exception = ProductNotFoundException.productNotFound("product1");

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(ProductApplicationErrorCode.PRODUCT_NOT_FOUND));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("상품 InvalidCommandException을 타입 기반으로 매핑한다")
    void resolveInvalidCommandException() {
        // given
        var provider = new ProductErrorCodeMappingProvider();
        var exception = InvalidCommandException.invalidInput("잘못된 입력");

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(ProductApplicationErrorCode.INVALID_INPUT));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
