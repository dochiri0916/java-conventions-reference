package com.dochiri.tooltestlab.review.application.port.out;

import com.dochiri.tooltestlab.review.domain.Review;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;

import java.util.List;
import java.util.Optional;

/**
 * 리뷰 저장소 Port (Application 소유).
 * <p>
 * 기술적 저장소 작업 실패는 {@link RepositoryOperationException}으로 전달한다.
 */
public interface ReviewRepositoryPort {
    Review save(Review review);

    Optional<Review> findByPublicId(String publicId);

    List<Review> findByProductId(String productPublicId);

    boolean existsByMemberIdAndProductId(String memberPublicId, String productPublicId);

    void deleteByPublicId(String publicId);
}
