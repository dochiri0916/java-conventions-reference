package com.dochiri.tooltestlab.category.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ProblemType;
import com.dochiri.tooltestlab.category.domain.exception.CategoryDomainErrorCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import com.dochiri.tooltestlab.category.application.exception.CategoryErrorCode;
import com.dochiri.tooltestlab.category.application.exception.InvalidCommandException;
import com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryIdException;
import com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryNameException;

class CategoryErrorCodeMappingProviderTest {

    @Test
    @DisplayName("카테고리 에러 코드 5개를 모두 매핑한다")
    void hasAllMappings() {
        // given
        var provider = new CategoryErrorCodeMappingProvider();

        // when
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // then
        assertThat(mappings).isNotNull().hasSize(5);
    }

    @Test
    @DisplayName("모든 매핑이 non-null status와 problemType을 가진다")
    void allMappingsHaveValidStatusAndProblemType() {
        // given
        var provider = new CategoryErrorCodeMappingProvider();
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // when & then
        assertThat(mappings.values()).allSatisfy(mapping -> {
            assertThat(mapping.status()).isNotNull();
            assertThat(mapping.problemType()).isNotNull();
        });
    }

    @Test
    @DisplayName("CATEGORY_ID_REQUIRED는 400 VALIDATION_FAILED로 매핑한다")
    void categoryIdRequired() {
        // given
        var provider = new CategoryErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(CategoryDomainErrorCode.CATEGORY_ID_REQUIRED));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("CATEGORY_NAME_REQUIRED는 400 VALIDATION_FAILED로 매핑한다")
    void categoryNameRequired() {
        // given
        var provider = new CategoryErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(CategoryDomainErrorCode.CATEGORY_NAME_REQUIRED));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("InvalidCategoryIdException을 타입 기반으로 매핑한다")
    void resolveInvalidCategoryIdException() {
        // given
        var provider = new CategoryErrorCodeMappingProvider();
        var exception = InvalidCategoryIdException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(CategoryDomainErrorCode.CATEGORY_ID_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mappedError.orElseThrow().mapping().problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("InvalidCategoryNameException을 타입 기반으로 매핑한다")
    void resolveInvalidCategoryNameException() {
        // given
        var provider = new CategoryErrorCodeMappingProvider();
        var exception = InvalidCategoryNameException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(CategoryDomainErrorCode.CATEGORY_NAME_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mappedError.orElseThrow().mapping().problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("InvalidCommandException을 타입 기반으로 매핑한다")
    void resolveInvalidCommandException() {
        // given
        var provider = new CategoryErrorCodeMappingProvider();
        var exception = InvalidCommandException.invalidInput("잘못된 입력");

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(
                CategoryErrorCode.INVALID_INPUT));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mappedError.orElseThrow().mapping().problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }
}
