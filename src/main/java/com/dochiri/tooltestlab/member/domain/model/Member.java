package com.dochiri.tooltestlab.member.domain.model;

import static java.util.Objects.requireNonNull;

public final class Member {

    private final MemberId id;
    private final Email email;
    private final EncodedPassword encodedPassword;
    private final MemberName name;
    private final MemberStatus status;

    private Member(
            final MemberId id,
            final Email email,
            final EncodedPassword encodedPassword,
            final MemberName name,
            final MemberStatus status
    ) {
        this.id = requireNonNull(id, "회원 ID는 null일 수 없습니다.");
        this.email = requireNonNull(email, "이메일은 null일 수 없습니다.");
        this.encodedPassword = requireNonNull(encodedPassword, "비밀번호는 null일 수 없습니다.");
        this.name = requireNonNull(name, "회원 이름은 null일 수 없습니다.");
        this.status = requireNonNull(status, "회원 상태는 null일 수 없습니다.");
    }

    public static Member create(
            final Email email,
            final EncodedPassword encodedPassword,
            final MemberName name
    ) {
        return new Member(MemberId.generate(), email, encodedPassword, name, MemberStatus.ACTIVE);
    }

    public static Member restore(
            final MemberId id,
            final Email email,
            final EncodedPassword encodedPassword,
            final MemberName name,
            final MemberStatus status
    ) {
        return new Member(id, email, encodedPassword, name, status);
    }

    public Member withdraw() {
        return new Member(id, email, encodedPassword, name, MemberStatus.WITHDRAWN);
    }

    public MemberId id() {
        return id;
    }

    public Email email() {
        return email;
    }

    public EncodedPassword encodedPassword() {
        return encodedPassword;
    }

    public MemberName name() {
        return name;
    }

    public MemberStatus status() {
        return status;
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
