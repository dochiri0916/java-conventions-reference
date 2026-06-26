package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.model.Review;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
class ReviewPersistenceAdapter implements ReviewRepositoryPort {

    private final ReviewJpaRepository reviewJpaRepository;

    ReviewPersistenceAdapter(ReviewJpaRepository reviewJpaRepository) {
        this.reviewJpaRepository = reviewJpaRepository;
    }

    @Override
    public Review save(Review review) {
        ReviewEntity entity = ReviewMapper.toEntity(review);
        ReviewEntity saved = reviewJpaRepository.save(entity);
        return ReviewMapper.toDomain(saved);
    }

    @Override
    public Optional<Review> findByPublicId(String publicId) {
        return reviewJpaRepository.findByPublicId(publicId).map(ReviewMapper::toDomain);
    }

    @Override
    public List<Review> findByProductId(String productPublicId) {
        return reviewJpaRepository.findByProductPublicIdOrderByIdDesc(productPublicId).stream()
                .map(ReviewMapper::toDomain).toList();
    }

    @Override
    public boolean existsByMemberIdAndProductId(String memberPublicId, String productPublicId) {
        return reviewJpaRepository.existsByMemberPublicIdAndProductPublicId(memberPublicId, productPublicId);
    }

    @Override
    public void deleteByPublicId(String publicId) {
        reviewJpaRepository.deleteByPublicId(publicId);
    }
}
