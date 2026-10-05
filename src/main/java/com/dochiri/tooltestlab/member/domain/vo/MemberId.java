package com.dochiri.tooltestlab.member.domain.vo;

import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException;
import com.dochiri.tooltestlab.member.domain.exception.MemberIdRequiredException;
import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public final class MemberId {

    private final UUID value;

    private MemberId(final UUID value) {
        if (value == null) {
            throw new MemberIdRequiredException();
        }

        if (value.version() != 7) {
            throw new InvalidMemberIdException();
        }
        this.value = value;
    }

    public static MemberId generate() {
        return new MemberId(
                UuidCreator.getTimeOrderedEpoch()
        );
    }

    public static MemberId of(final UUID value) {
        return new MemberId(value);
    }

    public static MemberId parse(final String value) {
        if (value == null) {
            throw new MemberIdRequiredException();
        }

        final UUID uuid;
        try {
            uuid = UUID.fromString(value);
        } catch (final IllegalArgumentException exception) {
            throw new InvalidMemberIdException();
        }

        if (!uuid.toString()
                .equalsIgnoreCase(value)) {
            throw new InvalidMemberIdException();
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
        if (!(object instanceof final MemberId other)) {
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
        return "MemberId[value=" + value + "]";
    }
}
