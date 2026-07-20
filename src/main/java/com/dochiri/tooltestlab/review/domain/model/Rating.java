package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.ReviewDomainException;

public record Rating(int value) {

    public Rating {
        if (value < 1 || value > 5) {
            throw ReviewDomainException.invalidRating(value);
        }
    }
}
