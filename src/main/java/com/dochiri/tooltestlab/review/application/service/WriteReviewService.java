package com.dochiri.tooltestlab.review.application.service;

import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.domain.model.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.model.MemberReferenceId;
import com.dochiri.tooltestlab.review.application.port.in.WriteReviewCommand;
import com.dochiri.tooltestlab.review.application.port.in.WriteReviewResult;
import com.dochiri.tooltestlab.review.application.port.in.WriteReviewUseCase;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.model.Rating;
import com.dochiri.tooltestlab.review.domain.model.Review;
import com.dochiri.tooltestlab.review.domain.model.ReviewContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class WriteReviewService implements WriteReviewUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;

    @Override
    @Transactional
    public WriteReviewResult write(final WriteReviewCommand command) {
        final MemberReferenceId memberId = new MemberReferenceId(command.memberPublicId());
        final ProductReferenceId productId = new ProductReferenceId(command.productPublicId());
        if (reviewRepositoryPort.existsByMemberIdAndProductId(memberId.value(), productId.value())) {
            throw new DuplicateReviewException();
        }

        final Review review = Review.create(
                memberId,
                productId,
                new Rating(command.rating()),
                new ReviewContent(command.content()));
        final Review saved = reviewRepositoryPort.save(review);

        return new WriteReviewResult(saved.id().value(), saved.memberId().value(),
                saved.productId().value(), saved.rating().value(), saved.content().value());
    }
}
