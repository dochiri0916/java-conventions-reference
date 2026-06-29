package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.domain.model.Email;
import com.dochiri.tooltestlab.member.domain.model.Member;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.member.domain.model.MemberName;
import com.dochiri.tooltestlab.member.domain.model.MemberStatus;
import com.dochiri.tooltestlab.member.domain.model.Password;

final class MemberMapper {

    private MemberMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    static Member toDomain(final MemberEntity entity) {
        return new Member(
                new MemberId(entity.getPublicId()),
                new Email(entity.getEmail()),
                new Password(entity.getPassword()),
                new MemberName(entity.getName()),
                MemberStatus.valueOf(entity.getStatus()));
    }

    static MemberEntity toEntity(final Member domain) {
        return new MemberEntity(
                null,
                domain.id().value(),
                domain.email().value(),
                domain.password().value(),
                domain.name().value(),
                domain.status().name());
    }
}
