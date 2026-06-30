package com.dochiri.tooltestlab.review.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewExceptionTest {

    @Test
    @DisplayName("InvalidReviewIdException 정상 생성")
    void invalidReviewId() {
            // given

            // when

            // then

        var ex = InvalidReviewIdException.invalidReviewId();
        assertThat(ex.code()).isEqualTo(ReviewDomainErrorCode.INVALID_REVIEW_ID);
    }

    @Test
    @DisplayName("InvalidReviewContentException 정상 생성")
    void badRvwContent() {
            // given

            // when

            // then

        var ex = InvalidReviewContentException.badRvwContent();
        assertThat(ex.code()).isEqualTo(ReviewDomainErrorCode.BAD_RVW_CONTENT);
    }

    @Test
    @DisplayName("InvalidRatingException 정상 생성")
    void invalidRating() {
            // given

            // when

            // then

        var ex = InvalidRatingException.invalidRating(6);
        assertThat(ex.code()).isEqualTo(ReviewDomainErrorCode.INVALID_RATING);
        assertThat(ex.rating()).isEqualTo(6);
    }

    @Test
    @DisplayName("ReviewDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
            // given

            // when

            // then

        for (var code : ReviewDomainErrorCode.values()) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
