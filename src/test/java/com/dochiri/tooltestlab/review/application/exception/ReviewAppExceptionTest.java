package com.dochiri.tooltestlab.review.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewAppExceptionTest {

    @Test
    @DisplayName("DuplicateReviewException 정상 생성")
    void duplicateReview() {
            // given

            // when

            // then

        var ex = DuplicateReviewException.duplicateReview();
        assertThat(ex.code()).isEqualTo(ReviewApplicationErrorCode.DUPLICATE_REVIEW);
    }

    @Test
    @DisplayName("NotReviewOwnerException 정상 생성")
    void notReviewOwner() {
            // given

            // when

            // then

        var ex = NotReviewOwnerException.notReviewOwner();
        assertThat(ex.code()).isEqualTo(ReviewApplicationErrorCode.NOT_REVIEW_OWNER);
    }

    @Test
    @DisplayName("ReviewNotFoundException 정상 생성")
    void reviewNotFound() {
            // given

            // when

            // then

        var ex = ReviewNotFoundException.reviewNotFound("rev123");
        assertThat(ex.code()).isEqualTo(ReviewApplicationErrorCode.REVIEW_NOT_FOUND);
        assertThat(ex.reviewPublicId()).isEqualTo("rev123");
    }

    @Test
    @DisplayName("ReviewApplicationErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
            // given

            // when

            // then

        for (var code : ReviewApplicationErrorCode.values()) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
