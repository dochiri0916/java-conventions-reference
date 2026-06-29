package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.InvalidRatingException;

public record Rating(int value) {

    public Rating {
        if (value < 1 || value > 5) {
            throw new InvalidRatingException(value);
        }
    }
}
