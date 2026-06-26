package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.domain.model.Member;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.member.domain.model.MemberStatus;

/**
 * MemberEntity ↔ Member 변환 유틸리티.
 * Spring Bean 등록 금지 (CLODE.md 규칙).
 */
final class MemberMapper {

    private MemberMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    static Member toDomain(MemberEntity entity) {
        return new Member(
                new MemberId(entity.getPublicId()),
                entity.getEmail(),
                entity.getPassword(),
                entity.getName(),
                MemberStatus.valueOf(entity.getStatus()));
    }

    static MemberEntity toEntity(Member domain) {
        return new MemberEntity(
                null,
                domain.id().value(),
                domain.email(),
                domain.password(),
                domain.name(),
                domain.status().name());
    }
}
