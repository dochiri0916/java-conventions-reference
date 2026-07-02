package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException;

import java.util.UUID;

public record MemberId(String value) {

    public MemberId {
        if (value == null) {
            throw InvalidMemberIdException.required();
        }
        final String stripped = value.strip();
        if (stripped.isBlank()) {
            throw InvalidMemberIdException.invalidMemberId();
        }
        value = stripped;
    }

    public static MemberId generate() {
        return new MemberId(UUID.randomUUID().toString().replace("-", ""));
    }
}
