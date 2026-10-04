package com.dochiri.tooltestlab.member.domain;

import com.dochiri.tooltestlab.member.domain.vo.Email;
import com.dochiri.tooltestlab.member.domain.event.MemberRegistered;
import com.dochiri.tooltestlab.member.domain.vo.EncodedPassword;
import com.dochiri.tooltestlab.member.domain.vo.MemberId;
import com.dochiri.tooltestlab.member.domain.vo.MemberName;
import com.dochiri.tooltestlab.member.domain.vo.MemberStatus;

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
        this.id = requireNonNull(id);
        this.email = requireNonNull(email);
        this.encodedPassword = requireNonNull(encodedPassword);
        this.name = requireNonNull(name);
        this.status = requireNonNull(status);
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

    public MemberRegistered registeredEvent() {
        return new MemberRegistered(id);
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
