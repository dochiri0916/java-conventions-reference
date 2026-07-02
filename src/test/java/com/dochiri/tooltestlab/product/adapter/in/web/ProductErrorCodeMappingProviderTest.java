package com.dochiri.tooltestlab.product.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ProblemType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProductErrorCodeMappingProviderTest {

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("LOW_STOCK");

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("PRODUCT_NOT_FOUND");

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("PRODUCT_ID_REQUIRED");

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("MONEY_NEGATIVE");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }
}
