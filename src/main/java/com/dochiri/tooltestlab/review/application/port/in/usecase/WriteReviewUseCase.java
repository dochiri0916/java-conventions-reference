package com.dochiri.tooltestlab.review.application.port.in.usecase;

import com.dochiri.tooltestlab.review.application.port.in.command.WriteReviewCommand;
import com.dochiri.tooltestlab.review.application.port.in.result.WriteReviewResult;

/**
 * 리뷰 작성 UseCase.
 */
@FunctionalInterface
public interface WriteReviewUseCase {
    WriteReviewResult write(WriteReviewCommand command);
}
