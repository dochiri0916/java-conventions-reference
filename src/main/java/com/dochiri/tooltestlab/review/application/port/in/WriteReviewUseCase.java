package com.dochiri.tooltestlab.review.application.port.in;

import com.dochiri.tooltestlab.review.application.command.WriteReviewCommand;
import com.dochiri.tooltestlab.review.application.result.WriteReviewResult;

/**
 * 리뷰 작성 UseCase.
 */
public interface WriteReviewUseCase {
    WriteReviewResult write(WriteReviewCommand command);
}
