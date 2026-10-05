package com.dochiri.tooltestlab.review.domain.vo;

import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewIdException;
import com.dochiri.tooltestlab.review.domain.exception.ReviewIdRequiredException;
import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public final class ReviewId {

    private final UUID value;

    private ReviewId(final UUID value) {
        if (value == null) {
            throw new ReviewIdRequiredException();
        }

        if (value.version() != 7) {
            throw new InvalidReviewIdException();
        }
        this.value = value;
    }

    public static ReviewId generate() {
        return new ReviewId(
                UuidCreator.getTimeOrderedEpoch()
        );
    }

    public static ReviewId of(final UUID value) {
        return new ReviewId(value);
    }

    public static ReviewId parse(final String value) {
        if (value == null) {
            throw new ReviewIdRequiredException();
        }

        final UUID uuid;
        try {
            uuid = UUID.fromString(value);
        } catch (final IllegalArgumentException exception) {
            throw new InvalidReviewIdException();
        }

        if (!uuid.toString()
                .equalsIgnoreCase(value)) {
            throw new InvalidReviewIdException();
        }

        return of(uuid);
    }

    public UUID reviewId() {
        return value;
    }

    public String value() {
        return value.toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final ReviewId other)) {
            return false;
        }
        return value.equals(
                other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "ReviewId[value=" + value + "]";
    }
}
