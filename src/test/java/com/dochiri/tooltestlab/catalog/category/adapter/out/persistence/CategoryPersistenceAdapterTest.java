package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.application.exception.DuplicateCategoryNameException;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import org.junit.jupiter.api.DisplayName;
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
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@Import({CategoryPersistenceAdapter.class, CategoryPersistenceAdapterTest.AuditingConfig.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CategoryPersistenceAdapterTest {

    @Autowired CategoryRepositoryPort categories;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired JdbcTemplate jdbc;

    @Test
    @DisplayName("사전 조회 없이 저장해도 중복 카테고리 이름을 거부한다")
    void rejectsDuplicateNameWithoutApplicationPrecheck() {
        // given
        final String name = uniqueName();
        create(category(name));

        // when
        // then
        assertThrows(DuplicateCategoryNameException.class, () -> create(category(name)));
        assertEquals(1L, count(name));
    }

    @Test
    @DisplayName("일괄 저장의 이름 중복을 번역하고 전체 저장을 롤백한다")
    void translatesDuplicateNameAndRollsBackBatch() {
        // given
        final String existingName = uniqueName();
        final String newName = uniqueName();
        create(category(existingName));
        final Set<Category> batch = Set.of(category(existingName), category(newName));

        // when
        // then
        assertThrows(DuplicateCategoryNameException.class, () -> createAll(batch));
        assertEquals(1L, count(existingName));
        assertEquals(0L, count(newName));
    }

    @Test
    @DisplayName("공개 식별자 중복은 이름 중복이 아닌 저장소 오류로 전달한다")
    void translatesDuplicatePublicIdAsRepositoryFailure() {
        // given
        final Category original = create(category(uniqueName()));
        final Category duplicateId = Category.restore(original.id(), CategoryName.of(uniqueName()), original.state());

        // when
        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> create(duplicateId));

        // then
        assertInstanceOf(org.springframework.dao.DataIntegrityViolationException.class, exception.getCause());
    }

    @Test
    @DisplayName("일괄 저장의 공개 식별자 중복도 저장소 오류로 전달한다")
    void translatesDuplicatePublicIdInBatchAsRepositoryFailure() {
        // given
        final Category original = create(category(uniqueName()));
        final Category duplicateId = Category.restore(original.id(), CategoryName.of(uniqueName()), original.state());
        final Set<Category> batch = Set.of(duplicateId, category(uniqueName()));

        // when
        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> createAll(batch));

        // then
        assertInstanceOf(org.springframework.dao.DataIntegrityViolationException.class, exception.getCause());
    }

    @Test
    @DisplayName("이름 컬럼 길이 초과는 저장소 오류로 전달한다")
    void translatesColumnLengthViolationAsRepositoryFailure() {
        // given
        final Category invalid = category("a".repeat(256));

        // when
        // then
        assertThrows(RepositoryOperationException.class, () -> create(invalid));
    }

    @Test
    @DisplayName("같은 이름을 동시에 저장하면 한 카테고리만 생성한다")
    void allowsOnlyOneConcurrentInsertForTheSameName() throws Exception {
        // given
        final String name = uniqueName();
        final CyclicBarrier barrier = new CyclicBarrier(2);

        // when
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final var first = executor.submit(() -> createConcurrently(category(name), barrier));
            final var second = executor.submit(() -> createConcurrently(category(name), barrier));

            // then
            final boolean firstCreated = first.get(10, TimeUnit.SECONDS);
            final boolean secondCreated = second.get(10, TimeUnit.SECONDS);
            assertNotEquals(firstCreated, secondCreated);
        }
        assertEquals(1L, count(name));
    }

    private boolean createConcurrently(final Category category, final CyclicBarrier barrier) throws Exception {
        barrier.await(5, TimeUnit.SECONDS);
        try {
            create(category);
            return true;
        } catch (final DuplicateCategoryNameException exception) {
            return false;
        }
    }

    private Category create(final Category category) {
        return new TransactionTemplate(transactionManager).execute(status -> categories.create(category));
    }

    private Set<Category> createAll(final Set<Category> batch) {
        return new TransactionTemplate(transactionManager).execute(status -> categories.createAll(batch));
    }

    private long count(final String name) {
        return jdbc.queryForObject("select count(*) from categories where name = ?", Long.class, name);
    }

    private static Category category(final String name) {
        return Category.create(CategoryName.of(name));
    }

    private static String uniqueName() {
        return UUID.randomUUID().toString();
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
