package com.dochiri.tooltestlab.review.application.port.out;

import com.dochiri.tooltestlab.review.domain.Review;

import java.util.List;

public record Reviews(List<Review> values) {

    public Reviews {
        values = List.copyOf(values);
    }

}
