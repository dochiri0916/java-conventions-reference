package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidPasswordException;

public record Password(String value) {

    public Password {
        if (value == null) {
            throw InvalidPasswordException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidPasswordException.invalidPassword();
        }
    }
}
