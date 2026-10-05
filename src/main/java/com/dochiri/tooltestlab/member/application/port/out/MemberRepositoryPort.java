package com.dochiri.tooltestlab.member.application.port.out;

import com.dochiri.tooltestlab.member.domain.Member;
import com.dochiri.tooltestlab.member.domain.vo.Email;
import com.dochiri.tooltestlab.member.domain.vo.MemberId;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;

import java.util.Optional;

public interface MemberRepositoryPort {

    Member create(Member member);

    Optional<Member> findByMemberId(MemberId memberId);

    Optional<Member> findByEmail(Email email);

    boolean existsByEmail(Email email);

}
