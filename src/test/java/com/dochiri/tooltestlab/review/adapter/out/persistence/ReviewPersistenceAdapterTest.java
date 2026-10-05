package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.Review;
import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.Rating;
import com.dochiri.tooltestlab.review.domain.vo.ReviewContent;
import com.dochiri.tooltestlab.review.domain.vo.ReviewId;
import com.github.f4b6a3.uuid.UuidCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@Import(ReviewPersistenceAdapterTest.AuditingConfig.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class ReviewPersistenceAdapterTest {

    @Autowired ReviewJpaRepository jpaRepository;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired JdbcTemplate jdbc;

    @ParameterizedTest(name = "safe={0}")
    @ValueSource(booleans = {false, true})
    @DisplayName("기본 및 안전 어댑터 모두 리뷰를 저장하고 VO로 조회 및 삭제한다")
    void createsFindsAndDeletesReview(final boolean safe) {
        // given
        final ReviewRepositoryPort reviews = adapter(safe);
        final Review original = create(reviews, review(memberId(), productId()));

        // when
        final Review restored = new TransactionTemplate(transactionManager)
                .execute(status -> reviews.findByReviewId(original.id()).orElseThrow());

        // then
        assertEquals(original.id(), restored.id());
        assertEquals(original.memberId(), restored.memberId());
        assertEquals(original.productId(), restored.productId());
        assertEquals(original.rating(), restored.rating());
        assertEquals(original.content(), restored.content());
        assertTrue(reviews.existsByMemberIdAndProductId(original.memberId(), original.productId()));
        assertEquals(java.util.List.of(original), reviews.findByProductId(original.productId()));
        assertEquals(original.id().value(), jdbc.queryForObject(
                "select public_id from reviews where member_id = ? and product_id = ?", String.class,
                original.memberId().value(), original.productId().value()));

        new TransactionTemplate(transactionManager)
                .executeWithoutResult(status -> reviews.deleteByReviewId(original.id()));

        assertTrue(reviews.findByReviewId(original.id()).isEmpty());
        assertFalse(reviews.existsByMemberIdAndProductId(original.memberId(), original.productId()));
    }

    @ParameterizedTest(name = "safe={0}")
    @ValueSource(booleans = {false, true})
    @DisplayName("삭제 반영 시 발생한 기술 오류도 저장소 오류로 전달한다")
    void translatesDeleteFlushFailureAsRepositoryFailure(final boolean safe) {
        // given
        final ReviewJpaRepository failingJpa = mock(ReviewJpaRepository.class);
        final ReviewRepositoryPort reviews = safe
                ? new SafeReviewPersistenceAdapter(failingJpa) : new ReviewPersistenceAdapter(failingJpa);
        final ReviewId reviewId = ReviewId.generate();
        final DataIntegrityViolationException failure = new DataIntegrityViolationException("delete failed");
        doThrow(failure).when(failingJpa).flush();

        // when
        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> reviews.deleteByReviewId(reviewId));

        // then
        assertSame(failure, exception.getCause());
        final var order = inOrder(failingJpa);
        order.verify(failingJpa).deleteByPublicId(reviewId.value());
        order.verify(failingJpa).flush();
    }

    @ParameterizedTest(name = "safe={0}")
    @ValueSource(booleans = {false, true})
    @DisplayName("회원과 상품이 같은 리뷰만 리뷰 중복 오류로 번역한다")
    void rejectsDuplicateMemberProductWithoutApplicationPrecheck(final boolean safe) {
        // given
        final ReviewRepositoryPort reviews = adapter(safe);
        final MemberReferenceId memberId = memberId();
        final ProductReferenceId productId = productId();
        create(reviews, review(memberId, productId));

        // when
        // then
        assertThrows(DuplicateReviewException.class, () -> create(reviews, review(memberId, productId)));
        assertEquals(1L, count(memberId, productId));
    }

    @ParameterizedTest(name = "safe={0}")
    @ValueSource(booleans = {false, true})
    @DisplayName("리뷰 공개 식별자 중복은 저장소 오류로 전달한다")
    void translatesDuplicatePublicIdAsRepositoryFailure(final boolean safe) {
        // given
        final ReviewRepositoryPort reviews = adapter(safe);
        final Review original = create(reviews, review(memberId(), productId()));
        final Review duplicateId = Review.restore(original.id(), memberId(), productId(),
                original.rating(), original.content());

        // when
        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> create(reviews, duplicateId));

        // then
        assertInstanceOf(DataIntegrityViolationException.class, exception.getCause());
        assertEquals(1L, count(original.memberId(), original.productId()));
        assertEquals(0L, count(duplicateId.memberId(), duplicateId.productId()));
    }

    @ParameterizedTest(name = "safe={0}")
    @ValueSource(booleans = {false, true})
    @DisplayName("리뷰 내용 컬럼 길이 초과는 저장소 오류로 전달한다")
    void translatesColumnLengthViolationAsRepositoryFailure(final boolean safe) {
        // given
        final ReviewRepositoryPort reviews = adapter(safe);
        final Review invalid = Review.create(memberId(), productId(), Rating.of(5),
                ReviewContent.of("a".repeat(1001)));

        // when
        // then
        assertThrows(RepositoryOperationException.class, () -> create(reviews, invalid));
    }

    @ParameterizedTest(name = "safe={0}")
    @ValueSource(booleans = {false, true})
    @DisplayName("같은 회원이 같은 상품에 동시에 작성하면 한 리뷰만 생성한다")
    void allowsOnlyOneConcurrentInsertForTheSameMemberProduct(final boolean safe) throws Exception {
        // given
        final ReviewRepositoryPort reviews = adapter(safe);
        final MemberReferenceId memberId = memberId();
        final ProductReferenceId productId = productId();
        final CyclicBarrier barrier = new CyclicBarrier(2);

        // when
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final var first = executor.submit(() -> createConcurrently(reviews, review(memberId, productId), barrier));
            final var second = executor.submit(() -> createConcurrently(reviews, review(memberId, productId), barrier));

            // then
            assertNotEquals(first.get(10, TimeUnit.SECONDS), second.get(10, TimeUnit.SECONDS));
        }
        assertEquals(1L, count(memberId, productId));
    }

    private boolean createConcurrently(
            final ReviewRepositoryPort reviews,
            final Review review,
            final CyclicBarrier barrier
    ) throws Exception {
        barrier.await(5, TimeUnit.SECONDS);
        try {
            create(reviews, review);
            return true;
        } catch (final DuplicateReviewException exception) {
            return false;
        }
    }

    private ReviewRepositoryPort adapter(final boolean safe) {
        return safe ? new SafeReviewPersistenceAdapter(jpaRepository) : new ReviewPersistenceAdapter(jpaRepository);
    }

    private Review create(final ReviewRepositoryPort reviews, final Review review) {
        return new TransactionTemplate(transactionManager).execute(status -> reviews.create(review));
    }

    private long count(final MemberReferenceId memberId, final ProductReferenceId productId) {
        return jdbc.queryForObject("select count(*) from reviews where member_id = ? and product_id = ?",
                Long.class, memberId.value(), productId.value());
    }

    private static Review review(final MemberReferenceId memberId, final ProductReferenceId productId) {
        return Review.create(memberId, productId, Rating.of(5), ReviewContent.of("리뷰 내용입니다"));
    }

    private static MemberReferenceId memberId() {
        return MemberReferenceId.of(UuidCreator.getTimeOrderedEpoch());
    }

    private static ProductReferenceId productId() {
        return ProductReferenceId.of(UuidCreator.getTimeOrderedEpoch());
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
