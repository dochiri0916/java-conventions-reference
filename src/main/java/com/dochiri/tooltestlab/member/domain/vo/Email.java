package com.dochiri.tooltestlab.member.domain.vo;

import com.dochiri.tooltestlab.member.domain.exception.EmailRequiredException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidEmailException;

import java.util.regex.Pattern;

public final class Email {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final String value;

    private Email(final String value) {
        if (value == null) {
            throw new EmailRequiredException();
        }

        final String normalized = value.strip();
        if (normalized.isBlank()) {
            throw new InvalidEmailException();
        }
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new InvalidEmailException();
        }
        this.value = normalized;
    }

    public static Email of(final String value) {
        return new Email(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final Email other)) {
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
        return "Email[value=" + value + "]";
    }
}
