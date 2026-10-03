package com.dochiri.tooltestlab.member.domain.vo;

import com.dochiri.tooltestlab.member.domain.exception.InvalidPasswordException;
import com.dochiri.tooltestlab.member.domain.exception.PasswordRequiredException;

public record EncodedPassword(String value) {

    public EncodedPassword {
        if (value == null) {
            throw new PasswordRequiredException();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw new InvalidPasswordException();
        }
    }
}
