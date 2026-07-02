package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidEmailException;

public record Email(String value) {

    public Email {
        if (value == null) {
            throw InvalidEmailException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidEmailException.invalidEmail();
        }
    }
}
