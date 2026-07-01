package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 동시성 안전 모드용 ReviewPersistenceAdapter.
 * {@code tool-test-lab.concurrency.safe=true}일 때 활성화.
 * 중복 리뷰 작성 시 {@link DataIntegrityViolationException}을 의미 있는 예외로 변환한다.
 */
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "true")
class SafeReviewPersistenceAdapter implements ReviewRepositoryPort {

    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public Review save(final Review review) {
        try {
            final ReviewEntity entity = ReviewMapper.toEntity(review);
            final ReviewEntity saved = reviewJpaRepository.save(entity);
            return ReviewMapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("이미 이 상품에 리뷰를 작성하셨습니다.", e);
        }
    }

    @Override
    public Optional<Review> findByPublicId(final String publicId) {
        return reviewJpaRepository.findByReviewId(publicId).map(ReviewMapper::toDomain);
    }

    @Override
    public List<Review> findByProductId(final String productPublicId) {
        return reviewJpaRepository.findByProductIdOrderByIdDesc(productPublicId).stream()
                .map(ReviewMapper::toDomain).toList();
    }

    @Override
    public boolean existsByMemberIdAndProductId(final String memberPublicId, final String productPublicId) {
        return reviewJpaRepository.existsByMemberIdAndProductId(memberPublicId, productPublicId);
    }

    @Override
    public void deleteByPublicId(final String publicId) {
        reviewJpaRepository.deleteByReviewId(publicId);
    }
}
