package com.dochiri.tooltestlab.review.domain.event;

import com.dochiri.tooltestlab.review.domain.vo.ReviewId;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;

public record ReviewDeleted(ReviewId reviewId, ProductReferenceId productId) {
}
