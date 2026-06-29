package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberNameException;

import static java.util.Objects.requireNonNull;

public record MemberName(String value) {

    public MemberName {
        requireNonNull(value, "이름은 null일 수 없습니다.");
        value = value.strip();
        if (value.isBlank()) {
            throw new InvalidMemberNameException();
        }
    }
}
