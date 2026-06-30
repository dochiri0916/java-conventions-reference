package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidEmailException;

import static java.util.Objects.requireNonNull;

public record Email(String value) {

    public Email {
        requireNonNull(value, "이메일은 null일 수 없습니다.");
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidEmailException.invalidEmail();
        }
    }
}
