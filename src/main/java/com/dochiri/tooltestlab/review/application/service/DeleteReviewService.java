package com.dochiri.tooltestlab.review.application.service;

import com.dochiri.tooltestlab.review.application.exception.ReviewNotFoundException;
import com.dochiri.tooltestlab.review.application.port.in.usecase.DeleteReviewUseCase;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.Review;
import com.dochiri.tooltestlab.review.domain.event.ReviewDeleted;
import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ReviewId;
import com.dochiri.tooltestlab.review.application.ReviewEventMapper;
import com.dochiri.tooltestlab.review.application.port.out.IntegrationEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class DeleteReviewService implements DeleteReviewUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public void delete(final String reviewPublicId, final String memberPublicId) {
        final ReviewId reviewId = ReviewId.parse(reviewPublicId);
        final MemberReferenceId memberId = MemberReferenceId.parse(memberPublicId);
        final Review review = reviewRepositoryPort.findByReviewId(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException());

        final ReviewDeleted deleted = review.delete(memberId);
        reviewRepositoryPort.deleteByReviewId(reviewId);
        integrationEventPort.publish(ReviewEventMapper.toIntegration(deleted));
    }
}
