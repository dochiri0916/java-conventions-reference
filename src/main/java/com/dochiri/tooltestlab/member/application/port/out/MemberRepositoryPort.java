package com.dochiri.tooltestlab.member.application.port.out;

import com.dochiri.tooltestlab.member.domain.model.Member;

import java.util.Optional;

/**
 * 회원 저장소 Port (Application 소유).
 */
public interface MemberRepositoryPort {
    Member save(Member member);

    Optional<Member> findByPublicId(String publicId);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
