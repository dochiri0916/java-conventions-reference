package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record MemberId(String value) {

    public MemberId {
        requireNonNull(value, "회원 ID는 null일 수 없습니다.");
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
