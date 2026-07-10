package com.dochiri.tooltestlab.review.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewExceptionTest {

    @Test
    @DisplayName("InvalidReviewIdException 정상 생성")
    void invalidReviewId() {
        // given
        var expectedCode = ReviewDomainErrorCode.INVALID_REVIEW_ID;

        // when
        var ex = InvalidReviewIdException.invalidReviewId();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidReviewIdException required 정상 생성")
    void reviewIdRequired() {
        // given
        var expectedCode = ReviewDomainErrorCode.REVIEW_ID_REQUIRED;

        // when
        var ex = InvalidReviewIdException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidReviewContentException 정상 생성")
    void badRvwContent() {
        // given
        var expectedCode = ReviewDomainErrorCode.BAD_RVW_CONTENT;

        // when
        var ex = InvalidReviewContentException.badRvwContent();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidReviewContentException required 정상 생성")
    void reviewContentRequired() {
        // given
        var expectedCode = ReviewDomainErrorCode.REVIEW_CONTENT_REQUIRED;

        // when
        var ex = InvalidReviewContentException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidRatingException 정상 생성")
    void invalidRating() {
        // given
        var rating = 6;
        var expectedCode = ReviewDomainErrorCode.INVALID_RATING;

        // when
        var ex = InvalidRatingException.invalidRating(rating);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.rating()).isEqualTo(rating);
    }

    @Test
    @DisplayName("InvalidRatingException required 정상 생성")
    void ratingRequired() {
        // given
        var expectedCode = ReviewDomainErrorCode.RATING_REQUIRED;

        // when
        var ex = InvalidRatingException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.rating()).isEqualTo(0);
    }

    @Test
    @DisplayName("InvalidReviewException 정상 생성")
    void invalidReview() {
        // given
        var expectedCode = ReviewDomainErrorCode.INVALID_REVIEW;

        // when
        var ex = InvalidReviewException.invalidReview();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidReviewException required 정상 생성")
    void reviewRequired() {
        // given
        var expectedCode = ReviewDomainErrorCode.REVIEW_REQUIRED;

        // when
        var ex = InvalidReviewException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("ReviewDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = ReviewDomainErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.name()).isNotBlank();
        }
    }
}
