package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.domain.model.*;

final class MemberMapper {

    private MemberMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    static Member toDomain(final MemberEntity entity) {
        return Member.reconstitute(
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
