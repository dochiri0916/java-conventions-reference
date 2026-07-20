package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.MemberDomainException;

import java.util.UUID;

public record MemberId(String value) {

    public MemberId {
        if (value == null) {
            throw MemberDomainException.memberIdRequired();
        }
        final String stripped = value.strip();
        if (stripped.isBlank()) {
            throw MemberDomainException.invalidMemberId(value);
        }
        value = stripped;
    }

    public static MemberId generate() {
        return new MemberId(UUID.randomUUID().toString().replace("-", ""));
    }
}
