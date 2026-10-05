package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.Review;
import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ReviewId;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;
import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.run;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "true")
class SafeReviewPersistenceAdapter implements ReviewRepositoryPort {

    private static final Pattern MEMBER_PRODUCT_CONSTRAINT = Pattern.compile(
            Pattern.quote("uk_reviews_member_product") + "(?:_INDEX_[0-9A-F]+)?",
            Pattern.CASE_INSENSITIVE);

    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public Review create(final Review review) {
        return execute(() -> {
            try {
                final ReviewEntity entity = ReviewMapper.toEntity(review);
                final ReviewEntity saved = reviewJpaRepository.saveAndFlush(entity);
                return ReviewMapper.toDomain(saved);
            } catch (final DataIntegrityViolationException exception) {
                if (isMemberProductConstraintViolation(exception)) {
                    throw new DuplicateReviewException();
                }
                throw exception;
            }
        });
    }

    @Override
    public Optional<Review> findByReviewId(final ReviewId reviewId) {
        return execute(() -> reviewJpaRepository.findByPublicId(reviewId.value()).map(ReviewMapper::toDomain));
    }

    @Override
    public List<Review> findByProductId(final ProductReferenceId productId) {
        return execute(() -> reviewJpaRepository.findByProductIdOrderByIdDesc(productId.value()).stream()
                .map(ReviewMapper::toDomain).toList());
    }

    @Override
    public boolean existsByMemberIdAndProductId(
            final MemberReferenceId memberId,
            final ProductReferenceId productId
    ) {
        return execute(() -> reviewJpaRepository.existsByMemberIdAndProductId(memberId.value(), productId.value()));
    }

    @Override
    public void deleteByReviewId(final ReviewId reviewId) {
        run(() -> {
            reviewJpaRepository.deleteByPublicId(reviewId.value());
            reviewJpaRepository.flush();
        });
    }

    private static boolean isMemberProductConstraintViolation(final Throwable exception) {
        for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
            if (cause instanceof ConstraintViolationException violation) {
                final String constraintName = violation.getConstraintName();
                if (constraintName == null) {
                    return false;
                }
                // H2는 스키마와 자동 생성한 유니크 인덱스 접미사를 함께 반환한다.
                final String unquoted = constraintName.replace("\"", "").replace("`", "");
                final String name = unquoted.substring(unquoted.lastIndexOf('.') + 1);
                return MEMBER_PRODUCT_CONSTRAINT.matcher(name).matches();
            }
        }
        return false;
    }
}
