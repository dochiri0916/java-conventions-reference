package com.dochiri.tooltestlab.member.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidEmailException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberNameException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidPasswordException;

public record Member(
        MemberId id,
        Email email,
        Password password,
        MemberName name,
        MemberStatus status) {

    public Member {
        if (id == null) {
            throw InvalidMemberIdException.required();
        }
        if (email == null) {
            throw InvalidEmailException.required();
        }
        if (password == null) {
            throw InvalidPasswordException.required();
        }
        if (name == null) {
            throw InvalidMemberNameException.required();
        }
        if (status == null) {
            throw InvalidMemberException.required();
        }
    }

    public static Member register(final Email email, final Password encodedPassword, final MemberName name) {
        return new Member(
                MemberId.generate(),
                email,
                encodedPassword,
                name,
                MemberStatus.ACTIVE
        );
    }

    public static Member reconstitute(
            final MemberId id, final Email email, final Password encodedPassword,
            final MemberName name, final MemberStatus status) {
        return new Member(
                id,
                email,
                encodedPassword,
                name,
                status
        );
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
