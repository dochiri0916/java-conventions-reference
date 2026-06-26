package com.dochiri.tooltestlab.member.domain.model;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record MemberId(String value) {

    public MemberId {
        requireNonNull(value, "회원 ID는 null일 수 없습니다.");
        if (value.isBlank()) throw new IllegalArgumentException("회원 ID는 빈 값일 수 없습니다.");
    }

    public static MemberId generate() {
        return new MemberId(UUID.randomUUID().toString().replace("-", ""));
    }
}
