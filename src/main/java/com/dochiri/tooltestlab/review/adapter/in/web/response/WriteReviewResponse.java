package com.dochiri.tooltestlab.review.adapter.in.web.response;

import com.dochiri.tooltestlab.review.application.port.in.result.WriteReviewResult;

public record WriteReviewResponse(String publicId, String message) {

    public static WriteReviewResponse from(final WriteReviewResult result) {
        return new WriteReviewResponse(result.publicId(), "리뷰가 등록되었습니다.");
    }
}
