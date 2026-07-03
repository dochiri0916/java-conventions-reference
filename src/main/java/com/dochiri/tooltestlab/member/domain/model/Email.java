package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidEmailException;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Email {
        if (value == null) {
            throw InvalidEmailException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidEmailException.invalidEmail();
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw InvalidEmailException.invalidEmail();
        }
    }
}
