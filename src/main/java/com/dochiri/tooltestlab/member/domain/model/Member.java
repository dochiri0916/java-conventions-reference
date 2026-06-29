package com.dochiri.tooltestlab.member.domain.model;

import static java.util.Objects.requireNonNull;

public record Member(
        MemberId id,
        Email email,
        Password password,
        MemberName name,
        MemberStatus status) {

    public Member {
        requireNonNull(id, "id는 null일 수 없습니다.");
        requireNonNull(email, "이메일은 null일 수 없습니다.");
        requireNonNull(password, "비밀번호는 null일 수 없습니다.");
        requireNonNull(name, "이름은 null일 수 없습니다.");
        requireNonNull(status, "회원 상태는 null일 수 없습니다.");
    }

    public static Member create(final Email email, final Password encodedPassword, final MemberName name) {
        return new Member(
                MemberId.generate(),
                email,
                encodedPassword,
                name,
                MemberStatus.ACTIVE);
    }

    public Member withdraw() {
        return new Member(id, email, password, name, MemberStatus.WITHDRAWN);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other
                || other instanceof Member member && id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
