package com.dochiri.tooltestlab.review.application.service;

import com.dochiri.tooltestlab.review.application.exception.NotReviewOwnerException;
import com.dochiri.tooltestlab.review.application.exception.ReviewNotFoundException;
import com.dochiri.tooltestlab.review.application.port.in.DeleteReviewUseCase;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class DeleteReviewService implements DeleteReviewUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;

    @Override
    @Transactional
    public void delete(final String reviewPublicId, final String memberPublicId) {
        final Review review = reviewRepositoryPort.findByPublicId(reviewPublicId)
                .orElseThrow(() -> ReviewNotFoundException.reviewNotFound(reviewPublicId));

        if (!review.memberId().value().equals(memberPublicId)) {
            throw NotReviewOwnerException.notReviewOwner();
        }

        reviewRepositoryPort.deleteByPublicId(reviewPublicId);
    }
}
