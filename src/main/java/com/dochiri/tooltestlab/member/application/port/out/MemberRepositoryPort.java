package com.dochiri.tooltestlab.member.application.port.out;

import com.dochiri.tooltestlab.member.domain.model.Member;

import java.util.Optional;

public interface MemberRepositoryPort {

    Member create(Member member);

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

}
