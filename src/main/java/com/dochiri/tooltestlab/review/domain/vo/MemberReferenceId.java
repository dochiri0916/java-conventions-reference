package com.dochiri.tooltestlab.review.domain.vo;

import com.dochiri.tooltestlab.review.domain.exception.InvalidMemberReferenceIdException;
import com.dochiri.tooltestlab.review.domain.exception.MemberReferenceIdRequiredException;

import java.util.UUID;

public final class MemberReferenceId {

    private final UUID value;

    private MemberReferenceId(final UUID value) {
        if (value == null) {
            throw new MemberReferenceIdRequiredException();
        }

        if (value.version() != 7) {
            throw new InvalidMemberReferenceIdException();
        }
        this.value = value;
    }

    public static MemberReferenceId of(final UUID value) {
        return new MemberReferenceId(value);
    }

    public static MemberReferenceId parse(final String value) {
        if (value == null) {
            throw new MemberReferenceIdRequiredException();
        }

        final UUID uuid;
        try {
            uuid = UUID.fromString(value);
        } catch (final IllegalArgumentException exception) {
            throw new InvalidMemberReferenceIdException();
        }

        if (!uuid.toString().equalsIgnoreCase(value)) {
            throw new InvalidMemberReferenceIdException();
        }

        return of(uuid);
    }

    public UUID memberId() {
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
        if (!(object instanceof final MemberReferenceId other)) {
            return false;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "MemberReferenceId[value=" + value + "]";
    }
}
