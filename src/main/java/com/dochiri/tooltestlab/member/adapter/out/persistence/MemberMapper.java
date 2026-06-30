package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.domain.model.*;

final class MemberMapper {

    private MemberMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    static Member toDomain(final MemberEntity entity) {
        return new Member(
                new MemberId(entity.getMemberId()),
                new Email(entity.getEmail()),
                new Password(entity.getPassword()),
                new MemberName(entity.getName()),
                MemberStatus.valueOf(entity.getStatus()));
    }

    static MemberEntity toEntity(final Member domain) {
        return MemberEntity.create(
                domain.id().value(),
                domain.email().value(),
                domain.password().value(),
                domain.name().value(),
                domain.status().name());
    }
}
