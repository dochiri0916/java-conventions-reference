package com.dochiri.tooltestlab.member.application.port.out;

import com.dochiri.tooltestlab.member.domain.Member;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;

import java.util.Optional;

/**
 * 기술적 저장소 작업 실패는 {@link RepositoryOperationException}으로 전달한다.
 */
public interface MemberRepositoryPort {

    Member create(Member member);

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

}
