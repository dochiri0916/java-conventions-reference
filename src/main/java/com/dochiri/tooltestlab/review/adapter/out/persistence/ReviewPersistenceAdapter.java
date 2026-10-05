package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.application.port.out.Reviews;
import com.dochiri.tooltestlab.review.domain.Review;
import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ReviewId;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;
import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.isConstraintViolation;
import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.run;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
final class ReviewPersistenceAdapter implements ReviewRepositoryPort {

    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public Review create(final Review review) {
        return execute(() -> {
            try {
                ReviewEntity entity = ReviewMapper.toEntity(review);
                final ReviewEntity saved = reviewJpaRepository.saveAndFlush(entity);
                return ReviewMapper.toDomain(saved);
            } catch (final DataIntegrityViolationException exception) {
                throw translateConflict(exception);
            }
        });
    }

    @Override
    public Optional<Review> findByReviewId(final ReviewId reviewId) {
        return execute(() -> reviewJpaRepository.findByPublicId(
                reviewId.value())
                        .map(ReviewMapper::toDomain));
    }

    @Override
    public Reviews findByProductId(final ProductReferenceId productId) {
        final String value = productId.value();
        return execute(() -> toReviews(reviewJpaRepository.findByProductIdOrderByIdDesc(value)));
    }

    private static Reviews toReviews(final List<ReviewEntity> entities) {
        final List<Review> reviews = entities.stream()
                .map(ReviewMapper::toDomain)
                .toList();
        return new Reviews(reviews);
    }

    @Override
    public boolean existsByMemberIdAndProductId(
            final MemberReferenceId memberId,
            final ProductReferenceId productId
    ) {
        return execute(() -> reviewJpaRepository.existsByMemberIdAndProductId(
                memberId.value(),
                productId.value()));
    }

    @Override
    public void deleteByReviewId(final ReviewId reviewId) {
        run(() -> {
            reviewJpaRepository.deleteByPublicId(
                    reviewId.value());
            reviewJpaRepository.flush();
        });
    }

    private static RuntimeException translateConflict(final DataIntegrityViolationException exception) {
        if (isConstraintViolation(exception, "uk_reviews_member_product")) {
            return new DuplicateReviewException(exception);
        }
        return exception;
    }
}
