package com.dochiri.tooltestlab.member.domain.vo;

import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException;
import com.dochiri.tooltestlab.member.domain.exception.MemberIdRequiredException;

import java.util.UUID;

public record MemberId(String value) {

    public MemberId {
        if (value == null) {
            throw new MemberIdRequiredException();
        }
        final String stripped = value.strip();
        if (stripped.isBlank()) {
            throw new InvalidMemberIdException();
        }
        value = stripped;
    }

    public static MemberId generate() {
        return new MemberId(UUID.randomUUID().toString().replace("-", ""));
    }
}
