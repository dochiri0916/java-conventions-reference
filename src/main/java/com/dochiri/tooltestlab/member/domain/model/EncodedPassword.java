package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidPasswordException;

public record EncodedPassword(String value) {

    public EncodedPassword {
        if (value == null) {
            throw InvalidPasswordException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidPasswordException.invalidPassword();
        }
    }
}
