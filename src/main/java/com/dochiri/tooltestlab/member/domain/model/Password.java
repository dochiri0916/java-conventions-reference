package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidPasswordException;

import static java.util.Objects.requireNonNull;

public record Password(String value) {

    public Password {
        requireNonNull(value, "비밀번호는 null일 수 없습니다.");
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidPasswordException.invalidPassword();
        }
    }
}
