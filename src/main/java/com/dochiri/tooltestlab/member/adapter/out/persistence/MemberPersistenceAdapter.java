package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.Member;
import com.dochiri.tooltestlab.member.domain.vo.Email;
import com.dochiri.tooltestlab.member.domain.vo.MemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;
import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.isConstraintViolation;

@Repository
@RequiredArgsConstructor
final class MemberPersistenceAdapter implements MemberRepositoryPort {

    private final MemberJpaRepository jpaRepository;

    @Override
    public Member create(final Member member) {
        return execute(() -> {
            try {
                MemberEntity entity = MemberMapper.toEntity(member);
                final MemberEntity saved = jpaRepository.saveAndFlush(entity);
                return MemberMapper.toDomain(saved);
            } catch (final DataIntegrityViolationException exception) {
                throw translateConflict(exception);
            }
        });
    }

    private static RuntimeException translateConflict(final DataIntegrityViolationException exception) {
        if (isConstraintViolation(exception, "uk_members_email")) {
            return new DuplicateEmailException(exception);
        }
        return exception;
    }

    @Override
    public Optional<Member> findByMemberId(final MemberId memberId) {
        return execute(() -> jpaRepository.findByPublicId(
                memberId.value())
                        .map(MemberMapper::toDomain));
    }

    @Override
    public Optional<Member> findByEmail(final Email email) {
        return execute(() -> jpaRepository.findByEmail(
                email.value())
                        .map(MemberMapper::toDomain));
    }

    @Override
    public boolean existsByEmail(final Email email) {
        return execute(() -> jpaRepository.existsByEmail(
                email.value()));
    }
}
