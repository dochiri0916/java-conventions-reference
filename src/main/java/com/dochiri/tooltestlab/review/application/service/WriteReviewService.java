package com.dochiri.tooltestlab.review.application.service;

import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.application.port.in.command.WriteReviewCommand;
import com.dochiri.tooltestlab.review.application.port.in.result.WriteReviewResult;
import com.dochiri.tooltestlab.review.application.port.in.usecase.WriteReviewUseCase;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.vo.Rating;
import com.dochiri.tooltestlab.review.domain.Review;
import com.dochiri.tooltestlab.review.application.ReviewEventMapper;
import com.dochiri.tooltestlab.review.application.ReviewResultMapper;
import com.dochiri.tooltestlab.review.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.review.domain.vo.ReviewContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class WriteReviewService implements WriteReviewUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public WriteReviewResult write(final WriteReviewCommand command) {
        final MemberReferenceId memberId = MemberReferenceId.parse(
                command.memberPublicId());
        final ProductReferenceId productId = ProductReferenceId.parse(
                command.productPublicId());
        if (reviewRepositoryPort.existsByMemberIdAndProductId(memberId, productId)) {
            throw new DuplicateReviewException();
        }

        final Review review = Review.create(
                memberId,
                productId,
                Rating.of(
                        command.rating()),
                ReviewContent.of(
                        command.content()));
        final Review saved = reviewRepositoryPort.create(review);
        integrationEventPort.publish(
                ReviewEventMapper.toIntegration(
                        saved.writtenEvent()));

        return ReviewResultMapper.toWriteResult(saved);
    }
}
