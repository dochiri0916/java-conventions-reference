package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.domain.Member;
import com.dochiri.tooltestlab.member.domain.vo.MemberStatus;
import com.dochiri.tooltestlab.member.domain.vo.Email;
import com.dochiri.tooltestlab.member.domain.vo.EncodedPassword;
import com.dochiri.tooltestlab.member.domain.vo.MemberId;
import com.dochiri.tooltestlab.member.domain.vo.MemberName;

final class MemberMapper {

    private MemberMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    static Member toDomain(final MemberEntity entity) {
        return Member.restore(
                MemberId.parse(
                        entity.getPublicId()),
                Email.of(
                        entity.getEmail()),
                EncodedPassword.of(
                        entity.getEncodedPassword()),
                MemberName.of(
                        entity.getName()),
                MemberStatus.valueOf(
                        entity.getStatus()));
    }

    static MemberEntity toEntity(final Member domain) {
        return MemberEntity.create(
                domain.id()
                        .value(),
                domain.email()
                        .value(),
                domain.encodedPassword()
                        .value(),
                domain.name()
                        .value(),
                domain.status()
                        .name());
    }
}
