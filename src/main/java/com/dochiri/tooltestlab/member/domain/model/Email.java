package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.MemberDomainException;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Email {
        if (value == null) {
            throw MemberDomainException.emailRequired();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw MemberDomainException.invalidEmail(value);
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw MemberDomainException.invalidEmail(value);
        }
    }
}
