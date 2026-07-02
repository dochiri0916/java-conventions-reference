package com.dochiri.tooltestlab.review.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ProblemType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewErrorCodeMappingProviderTest {

    @Test
    @DisplayName("리뷰 에러 코드 12개를 모두 매핑한다")
    void hasAllMappings() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();

        // when
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // then
        assertThat(mappings).isNotNull().hasSize(12);
    }

    @Test
    @DisplayName("모든 매핑이 non-null status와 problemType을 가진다")
    void allMappingsHaveValidStatusAndProblemType() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // when & then
        assertThat(mappings.values()).allSatisfy(mapping -> {
            assertThat(mapping.status()).isNotNull();
            assertThat(mapping.problemType()).isNotNull();
        });
    }

    @Test
    @DisplayName("REVIEW_NOT_FOUND는 404 NOT_FOUND로 매핑한다")
    void reviewNotFound() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("REVIEW_NOT_FOUND");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.NOT_FOUND);
    }

    @Test
    @DisplayName("NOT_REVIEW_OWNER는 403 FORBIDDEN으로 매핑한다")
    void notReviewOwner() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("NOT_REVIEW_OWNER");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.FORBIDDEN);
    }

    @Test
    @DisplayName("DUPLICATE_REVIEW는 409 CONFLICT로 매핑한다")
    void duplicateReview() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("DUPLICATE_REVIEW");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.CONFLICT);
    }

    @Test
    @DisplayName("REVIEW_ID_REQUIRED는 400 VALIDATION_FAILED로 매핑한다")
    void reviewIdRequired() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("REVIEW_ID_REQUIRED");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }
}
