package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.Member;
import com.dochiri.tooltestlab.member.domain.vo.Email;
import com.dochiri.tooltestlab.member.domain.vo.EncodedPassword;
import com.dochiri.tooltestlab.member.domain.vo.MemberName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@Import({MemberPersistenceAdapter.class, MemberPersistenceAdapterTest.AuditingConfig.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class MemberPersistenceAdapterTest {

    @Autowired MemberRepositoryPort members;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired JdbcTemplate jdbc;

    @Test
    void storesAndLoadsMemberIdThroughPublicIdColumn() {
        final Member member = create(member(uniqueEmail()));

        final String publicId = jdbc.queryForObject(
                "select public_id from members where email = ?", String.class, member.email().value());
        final Member loaded = members.findByMemberId(member.id()).orElseThrow();

        assertEquals(member.id().value(), publicId);
        assertEquals(member.id(), loaded.id());
        assertEquals(member.email(), loaded.email());
    }

    @Test
    void rejectsDuplicateEmailWithoutApplicationPrecheck() {
        final String email = uniqueEmail();
        create(member(email));

        assertThrows(DuplicateEmailException.class, () -> create(member(email)));
        assertEquals(1L, count(email));
    }

    @Test
    void translatesDuplicateMemberIdAsRepositoryFailure() {
        final Member original = create(member(uniqueEmail()));
        final Member duplicateId = Member.restore(original.id(), Email.of(uniqueEmail()),
                original.encodedPassword(), original.name(), original.status());

        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> create(duplicateId));

        assertInstanceOf(org.springframework.dao.DataIntegrityViolationException.class, exception.getCause());
    }

    @Test
    void translatesColumnLengthViolationAsRepositoryFailure() {
        final Member invalid = Member.create(Email.of(uniqueEmail()),
                EncodedPassword.of("encoded-password"), MemberName.of("a".repeat(256)));

        assertThrows(RepositoryOperationException.class, () -> create(invalid));
    }

    @Test
    void allowsOnlyOneConcurrentInsertForTheSameEmail() throws Exception {
        final String email = uniqueEmail();
        final CyclicBarrier barrier = new CyclicBarrier(2);

        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final var first = executor.submit(() -> createConcurrently(member(email), barrier));
            final var second = executor.submit(() -> createConcurrently(member(email), barrier));

            final boolean firstCreated = first.get(10, TimeUnit.SECONDS);
            final boolean secondCreated = second.get(10, TimeUnit.SECONDS);
            assertNotEquals(firstCreated, secondCreated);
        }
        assertEquals(1L, count(email));
    }

    private boolean createConcurrently(final Member member, final CyclicBarrier barrier) throws Exception {
        barrier.await(5, TimeUnit.SECONDS);
        try {
            create(member);
            return true;
        } catch (final DuplicateEmailException exception) {
            return false;
        }
    }

    private Member create(final Member member) {
        return new TransactionTemplate(transactionManager).execute(status -> members.create(member));
    }

    private long count(final String email) {
        return jdbc.queryForObject("select count(*) from members where email = ?", Long.class, email);
    }

    private static Member member(final String email) {
        return Member.create(Email.of(email), EncodedPassword.of("encoded-password"), MemberName.of("회원"));
    }

    private static String uniqueEmail() {
        return UUID.randomUUID() + "@example.com";
    }

    @TestConfiguration
    @EnableJpaAuditing
    static class AuditingConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("test");
        }
    }
}
