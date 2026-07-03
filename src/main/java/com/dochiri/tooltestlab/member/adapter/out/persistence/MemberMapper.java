package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.domain.model.Email;
import com.dochiri.tooltestlab.member.domain.model.EncodedPassword;
import com.dochiri.tooltestlab.member.domain.model.Member;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.member.domain.model.MemberName;
import com.dochiri.tooltestlab.member.domain.model.MemberStatus;

final class MemberMapper {

    private MemberMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    static Member toDomain(final MemberEntity entity) {
        return Member.reconstitute(
                new MemberId(entity.getMemberId()),
                new Email(entity.getEmail()),
                new EncodedPassword(entity.getEncodedPassword()),
                new MemberName(entity.getName()),
                MemberStatus.valueOf(entity.getStatus()));
    }

    static MemberEntity toEntity(final Member domain) {
        return MemberEntity.create(
                domain.id().value(),
                domain.email().value(),
                domain.encodedPassword().value(),
                domain.name().value(),
                domain.status().name());
    }
}
