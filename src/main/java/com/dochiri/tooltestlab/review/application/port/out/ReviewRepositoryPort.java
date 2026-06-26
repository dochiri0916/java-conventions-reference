package com.dochiri.tooltestlab.review.application.port.out;

import com.dochiri.tooltestlab.review.domain.model.Review;

import java.util.List;
import java.util.Optional;

/**
 * 리뷰 저장소 Port (Application 소유).
 */
public interface ReviewRepositoryPort {
    Review save(Review review);

    Optional<Review> findByPublicId(String publicId);

    List<Review> findByProductId(String productPublicId);

    boolean existsByMemberIdAndProductId(String memberPublicId, String productPublicId);

    void deleteByPublicId(String publicId);
}
