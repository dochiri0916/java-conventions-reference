package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.MemberDomainException;

public record MemberName(String value) {

    public MemberName {
        if (value == null) {
            throw MemberDomainException.memberNameRequired();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw MemberDomainException.invalidMemberName(value);
        }
    }
}
