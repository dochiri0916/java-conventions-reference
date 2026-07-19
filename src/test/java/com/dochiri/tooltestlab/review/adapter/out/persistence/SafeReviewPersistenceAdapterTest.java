package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.application.exception.ReviewApplicationErrorCode;
import com.dochiri.tooltestlab.review.domain.model.Rating;
import com.dochiri.tooltestlab.review.domain.model.Review;
import com.dochiri.tooltestlab.review.domain.model.ReviewContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SafeReviewPersistenceAdapterTest {

    @Mock
    private ReviewJpaRepository reviewJpaRepository;

    private SafeReviewPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SafeReviewPersistenceAdapter(reviewJpaRepository);
    }

    @Test
    @DisplayName("안전 리뷰 저장도 일반 리뷰 저장과 같은 중복 예외 계약을 사용한다")
    void translatesDuplicateReviewConstraintViolation() {
        // given
        final Review review = Review.create(
                new MemberId("member-1"),
                new ProductId("product-1"),
                new Rating(5),
                new ReviewContent("좋은 상품입니다."));
        when(reviewJpaRepository.saveAndFlush(any(ReviewEntity.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate review"));

        // when & then
        assertThatThrownBy(() -> adapter.save(review))
                .isInstanceOf(DuplicateReviewException.class)
                .extracting(exception -> ((DuplicateReviewException) exception).code())
                .isEqualTo(ReviewApplicationErrorCode.DUPLICATE_REVIEW);
    }
}
