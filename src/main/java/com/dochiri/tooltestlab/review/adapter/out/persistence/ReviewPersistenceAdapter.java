package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;
import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.run;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
class ReviewPersistenceAdapter implements ReviewRepositoryPort {

    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public Review save(final Review review) {
        return execute(() -> {
            try {
                final ReviewEntity entity = ReviewMapper.toEntity(review);
                final ReviewEntity saved = reviewJpaRepository.saveAndFlush(entity);
                return ReviewMapper.toDomain(saved);
            } catch (final DataIntegrityViolationException exception) {
                throw new DuplicateReviewException();
            }
        });
    }

    @Override
    public Optional<Review> findByPublicId(final String publicId) {
        return execute(() -> reviewJpaRepository.findByReviewId(publicId).map(ReviewMapper::toDomain));
    }

    @Override
    public List<Review> findByProductId(final String productPublicId) {
        return execute(() -> reviewJpaRepository.findByProductIdOrderByIdDesc(productPublicId).stream()
                .map(ReviewMapper::toDomain).toList());
    }

    @Override
    public boolean existsByMemberIdAndProductId(final String memberPublicId, final String productPublicId) {
        return execute(() -> reviewJpaRepository.existsByMemberIdAndProductId(memberPublicId, productPublicId));
    }

    @Override
    public void deleteByPublicId(final String publicId) {
        run(() -> reviewJpaRepository.deleteByReviewId(publicId));
    }
}
