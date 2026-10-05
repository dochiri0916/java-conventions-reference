package com.dochiri.tooltestlab.member.domain.vo;

import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberNameException;
import com.dochiri.tooltestlab.member.domain.exception.MemberNameRequiredException;

public final class MemberName {

    private final String value;

    private MemberName(final String value) {
        if (value == null) {
            throw new MemberNameRequiredException();
        }

        final String normalized = value.strip();
        if (normalized.isBlank()) {
            throw new InvalidMemberNameException();
        }
        this.value = normalized;
    }

    public static MemberName of(final String value) {
        return new MemberName(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final MemberName other)) {
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
        return "MemberName[value=" + value + "]";
    }
}
