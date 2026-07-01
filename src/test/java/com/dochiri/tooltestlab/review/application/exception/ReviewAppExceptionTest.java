package com.dochiri.tooltestlab.review.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewAppExceptionTest {

    @Test
    @DisplayName("DuplicateReviewException 정상 생성")
    void duplicateReview() {
        // given
        var expectedCode = ReviewApplicationErrorCode.DUPLICATE_REVIEW;

        // when
        var ex = DuplicateReviewException.duplicateReview();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("NotReviewOwnerException 정상 생성")
    void notReviewOwner() {
        // given
        var expectedCode = ReviewApplicationErrorCode.NOT_REVIEW_OWNER;

        // when
        var ex = NotReviewOwnerException.notReviewOwner();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("ReviewNotFoundException 정상 생성")
    void reviewNotFound() {
        // given
        var reviewPublicId = "rev123";
        var expectedCode = ReviewApplicationErrorCode.REVIEW_NOT_FOUND;

        // when
        var ex = ReviewNotFoundException.reviewNotFound(reviewPublicId);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.reviewPublicId()).isEqualTo(reviewPublicId);
    }

    @Test
    @DisplayName("ReviewApplicationErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = ReviewApplicationErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
