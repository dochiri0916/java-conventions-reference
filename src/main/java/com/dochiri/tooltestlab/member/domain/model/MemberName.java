package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberNameException;

public record MemberName(String value) {

    public MemberName {
        if (value == null) {
            throw InvalidMemberNameException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidMemberNameException.invalidMbrName();
        }
    }
}
