package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 동시성 안전 모드용 MemberPersistenceAdapter.
 * {@code tool-test-lab.concurrency.safe=true}일 때 활성화.
 * 이메일 중복 체크 시 PESSIMISTIC_WRITE 락으로 동시 등록을 방지한다.
 */
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "true")
class SafeMemberPersistenceAdapter implements MemberRepositoryPort {

    private final MemberJpaRepository jpaRepository;

    @Override
    public Member save(final Member member) {
        final MemberEntity entity = MemberMapper.toEntity(member);
        final MemberEntity saved = jpaRepository.save(entity);
        return MemberMapper.toDomain(saved);
    }

    @Override
    public Optional<Member> findByPublicId(final String publicId) {
        return jpaRepository.findByMemberId(publicId).map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        return jpaRepository.findByEmail(email).map(MemberMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(final String email) {
        // PESSIMISTIC_WRITE 락을 잡고 조회 → 동시에 같은 이메일로 등록하는 요청 직렬화
        return jpaRepository.findByEmailForUpdate(email).isPresent();
    }
}
