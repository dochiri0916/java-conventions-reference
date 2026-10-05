package com.dochiri.tooltestlab.review.application.port.out;

import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.domain.Review;
import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ReviewId;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;

import java.util.Optional;

/**
 * 리뷰 저장소 Port (Application 소유).
 * <p>
 * 회원과 상품이 같은 리뷰를 중복 생성하면 {@link DuplicateReviewException}으로 전달한다.
 * 기술적 저장소 작업 실패는 {@link RepositoryOperationException}으로 전달한다.
 */
public interface ReviewRepositoryPort {
    Review create(Review review);

    Optional<Review> findByReviewId(ReviewId reviewId);

    Reviews findByProductId(ProductReferenceId productId);

    boolean existsByMemberIdAndProductId(MemberReferenceId memberId, ProductReferenceId productId);

    void deleteByReviewId(ReviewId reviewId);
}
