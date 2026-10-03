package com.dochiri.tooltestlab.review.domain.vo;

import com.dochiri.tooltestlab.review.domain.exception.InvalidRatingException;

public final class Rating {

    private final int value;

    private Rating(final int value) {
        if (value < 1 || value > 5) {
            throw new InvalidRatingException();
        }
        this.value = value;
    }

    public static Rating of(final int value) {
        return new Rating(value);
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final Rating other)) {
            return false;
        }
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return "Rating[value=" + value + "]";
    }

}
