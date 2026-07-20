package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.MemberDomainException;

public record EncodedPassword(String value) {

    public EncodedPassword {
        if (value == null) {
            throw MemberDomainException.passwordRequired();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw MemberDomainException.invalidPassword();
        }
    }
}
