package com.dochiri.tooltestlab.review.application.port.in;

/**
 * 리뷰 작성 UseCase.
 */
public interface WriteReviewUseCase {
    WriteReviewResult write(WriteReviewCommand command);
}
