package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.vo.*;
import org.junit.jupiter.api.Test;
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
import com.github.f4b6a3.uuid.UuidCreator;

import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@Import({OrderPersistenceAdapter.class, SafeOrderPersistenceAdapter.class,
        OrderPersistenceAdapterTestBase.AuditingConfig.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
abstract class OrderPersistenceAdapterTestBase {

    @Autowired OrderRepositoryPort orders;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired JdbcTemplate jdbc;

    @Test
    void storesPublicIdsAndFindsOrdersThroughDomainIds() {
        final Order original = transaction(() -> orders.create(order("상품")));

        final Order loaded = transaction(() -> orders.findByOrderId(original.id()).orElseThrow());
        final Order locked = transaction(() -> orders.findByOrderIdForUpdate(original.id()).orElseThrow());
        final var memberOrders = transaction(() -> orders.findByMemberId(original.memberId()));
        final Order paid = transaction(() -> orders.update(loaded.pay()));

        assertEquals(original.id(), loaded.id());
        assertEquals(original.id(), locked.id());
        assertEquals(original.items(), loaded.items());
        assertEquals(1, memberOrders.size());
        assertEquals(OrderStatus.PAID, paid.status());
        assertEquals(OrderStatus.PAID, transaction(() -> orders.findByOrderId(original.id()).orElseThrow()).status());
        assertEquals(original.id().value(), jdbc.queryForObject(
                "select public_id from orders where member_id = ?", String.class, original.memberId().value()));
        assertNotNull(jdbc.queryForObject(
                "select public_id from order_lines where order_id = ?", String.class, original.id().value()));
    }

    @Test
    void duplicatePublicIdIsARepositoryFailureAndPreservesExistingLines() {
        final Order original = transaction(() -> orders.create(order("상품")));

        final RepositoryOperationException exception = assertThrows(RepositoryOperationException.class,
                () -> transaction(() -> orders.create(original)));

        assertInstanceOf(DataIntegrityViolationException.class, exception.getCause());
        assertEquals(original.items(), transaction(() -> orders.findByOrderId(original.id()).orElseThrow()).items());
    }

    @Test
    void failedLineInsertRollsBackTheWholeOrder() {
        final Order invalid = order("a".repeat(256));

        assertThrows(RepositoryOperationException.class, () -> transaction(() -> orders.create(invalid)));

        assertEquals(0L, jdbc.queryForObject(
                "select count(*) from orders where public_id = ?", Long.class, invalid.id().value()));
        assertEquals(0L, jdbc.queryForObject(
                "select count(*) from order_lines where order_id = ?", Long.class, invalid.id().value()));
    }

    private <T> T transaction(final Supplier<T> action) {
        return new TransactionTemplate(transactionManager).execute(status -> action.get());
    }

    private static Order order(final String productName) {
        return Order.create(MemberReferenceId.parse(UuidCreator.getTimeOrderedEpoch().toString()),
                CartReferenceId.parse(UuidCreator.getTimeOrderedEpoch().toString()), 1,
                OrderLines.of(OrderLine.of(ProductReferenceId.parse(UuidCreator.getTimeOrderedEpoch().toString()),
                        ProductName.of(productName), Quantity.of(2), Money.of(100))),
                ShippingAddress.of(RecipientName.of("홍길동"), Phone.of("010-1234-5678"),
                        StreetAddress.of("서울시 주소"), ZipCode.of("12345")));
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
