package com.dochiri.tooltestlab.member.domain.vo;

import com.dochiri.tooltestlab.member.domain.exception.InvalidPasswordException;
import com.dochiri.tooltestlab.member.domain.exception.PasswordRequiredException;

public final class EncodedPassword {

    private final String value;

    private EncodedPassword(final String value) {
        if (value == null) {
            throw new PasswordRequiredException();
        }

        final String normalized = value.strip();
        if (normalized.isBlank()) {
            throw new InvalidPasswordException();
        }
        this.value = normalized;
    }

    public static EncodedPassword of(final String value) {
        return new EncodedPassword(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final EncodedPassword other)) {
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
        return "EncodedPassword[value=" + value + "]";
    }
}
