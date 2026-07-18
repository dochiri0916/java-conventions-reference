package com.dochiri.tooltestlab.review.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ProblemType;
import com.dochiri.tooltestlab.review.application.exception.*;
import com.dochiri.tooltestlab.review.domain.exception.*;
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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(ReviewApplicationErrorCode.REVIEW_NOT_FOUND));

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(ReviewApplicationErrorCode.NOT_REVIEW_OWNER));

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(ReviewApplicationErrorCode.DUPLICATE_REVIEW));

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(ReviewDomainErrorCode.REVIEW_ID_REQUIRED));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("InvalidReviewIdException을 타입 기반으로 매핑한다")
    void resolveInvalidReviewIdException() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();
        var exception = InvalidReviewIdException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ReviewDomainErrorCode.REVIEW_ID_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidRatingException을 타입 기반으로 매핑한다")
    void resolveInvalidRatingException() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();
        var exception = InvalidRatingException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ReviewDomainErrorCode.RATING_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidReviewContentException을 타입 기반으로 매핑한다")
    void resolveInvalidReviewContentException() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();
        var exception = InvalidReviewContentException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ReviewDomainErrorCode.REVIEW_CONTENT_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidReviewException을 타입 기반으로 매핑한다")
    void resolveInvalidReviewException() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();
        var exception = InvalidReviewException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ReviewDomainErrorCode.REVIEW_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("ReviewNotFoundException을 타입 기반으로 매핑한다")
    void resolveReviewNotFoundException() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();
        var exception = ReviewNotFoundException.reviewNotFound("review1");

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ReviewApplicationErrorCode.REVIEW_NOT_FOUND));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("DuplicateReviewException을 타입 기반으로 매핑한다")
    void resolveDuplicateReviewException() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();
        var exception = DuplicateReviewException.duplicateReview();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ReviewApplicationErrorCode.DUPLICATE_REVIEW));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("NotReviewOwnerException을 타입 기반으로 매핑한다")
    void resolveNotReviewOwnerException() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();
        var exception = NotReviewOwnerException.notReviewOwner();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ReviewApplicationErrorCode.NOT_REVIEW_OWNER));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("리뷰 InvalidCommandException을 타입 기반으로 매핑한다")
    void resolveInvalidCommandException() {
        // given
        var provider = new ReviewErrorCodeMappingProvider();
        var exception = InvalidCommandException.invalidInput("잘못된 입력");

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(ReviewApplicationErrorCode.INVALID_INPUT));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
