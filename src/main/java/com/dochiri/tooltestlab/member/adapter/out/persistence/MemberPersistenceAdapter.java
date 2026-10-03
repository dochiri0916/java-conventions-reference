package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;

@Repository
@RequiredArgsConstructor
class MemberPersistenceAdapter implements MemberRepositoryPort {

    private final MemberJpaRepository jpaRepository;

    @Override
    public Member create(final Member member) {
        return execute(() -> {
            try {
                final MemberEntity entity = MemberMapper.toEntity(member);
                final MemberEntity saved = jpaRepository.saveAndFlush(entity);
                return MemberMapper.toDomain(saved);
            } catch (final DataIntegrityViolationException exception) {
                throw new DuplicateEmailException();
            }
        });
    }

    @Override
    public Optional<Member> findByMemberId(final String memberId) {
        return execute(() -> jpaRepository.findByMemberId(memberId).map(MemberMapper::toDomain));
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        return execute(() -> jpaRepository.findByEmail(email).map(MemberMapper::toDomain));
    }

    @Override
    public boolean existsByEmail(final String email) {
        return execute(() -> jpaRepository.existsByEmail(email));
    }
}
