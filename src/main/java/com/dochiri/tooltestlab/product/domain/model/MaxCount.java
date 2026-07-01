package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.product.domain.exception.InvalidMaxCountException;

public record MaxCount(int value) {

    public MaxCount {
        if (value <= 0) {
            throw InvalidMaxCountException.notPositive(value);
        }
    }

    public static MaxCount of(final int value) {
        return new MaxCount(value);
    }
}
