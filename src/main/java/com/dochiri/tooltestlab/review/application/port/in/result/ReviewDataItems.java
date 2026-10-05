package com.dochiri.tooltestlab.review.application.port.in.result;

import java.util.List;

public record ReviewDataItems(List<GetProductReviewsResult.ReviewData> values) {

    public ReviewDataItems {
        values = List.copyOf(values);
    }

}
