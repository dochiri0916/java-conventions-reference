package com.dochiri.tooltestlab.review.domain.event;

import com.dochiri.tooltestlab.review.domain.exception.ReviewIdRequiredException;
import com.dochiri.tooltestlab.review.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.review.domain.vo.ReviewId;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;

public record ReviewDeleted(ReviewId reviewId, ProductReferenceId productId) {

    public ReviewDeleted {
        if (reviewId == null) {
            throw new ReviewIdRequiredException();
        }
        if (productId == null) {
            throw new ProductReferenceIdRequiredException();
        }
    }

}
