package com.dochiri.tooltestlab.order.adapter.out.event;

import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.event.OrderCancelled;
import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;
import com.dochiri.tooltestlab.order.domain.model.OrderCancelledItems;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.OrderPlacedItems;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class SpringOrderEventAdapterTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @AfterEach
    void tearDown() {
        TransactionSynchronizationManager.setActualTransactionActive(false);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    @DisplayName("트랜잭션이 없으면 주문 생성 이벤트를 즉시 발행한다")
    void publishPlacedEventImmediatelyWithoutTransaction() {
        // given
        final OrderPlaced event = orderPlaced();
        final SpringOrderEventAdapter adapter = new SpringOrderEventAdapter(eventPublisher);

        // when
        adapter.publishAfterCommit(event);

        // then
        assertThat(TransactionSynchronizationManager.isActualTransactionActive()).isFalse();
        verify(eventPublisher).publishEvent(event);
    }

    @Test
    @DisplayName("트랜잭션이 있으면 주문 취소 이벤트를 커밋 이후 발행한다")
    void publishCancelledEventAfterCommitWhenTransactionActive() {
        // given
        final OrderCancelled event = orderCancelled();
        final SpringOrderEventAdapter adapter = new SpringOrderEventAdapter(eventPublisher);
        TransactionSynchronizationManager.initSynchronization();
        TransactionSynchronizationManager.setActualTransactionActive(true);

        // when
        adapter.publishAfterCommit(event);

        // then
        assertThat(TransactionSynchronizationManager.getSynchronizations()).hasSize(1);
        verifyNoInteractions(eventPublisher);
        final TransactionSynchronization synchronization = TransactionSynchronizationManager
                .getSynchronizations()
                .get(0);
        synchronization.afterCommit();
        verify(eventPublisher).publishEvent(event);
    }

    private OrderPlaced orderPlaced() {
        final OrderPlaced.ItemDetail itemDetail = new OrderPlaced.ItemDetail(
                ProductId.generate(),
                new ProductName("상품"),
                Quantity.from(1),
                new Money(1_000)
        );
        return new OrderPlaced(
                OrderId.generate(),
                MemberId.generate(),
                new OrderPlacedItems(List.of(itemDetail))
        );
    }

    private OrderCancelled orderCancelled() {
        final OrderCancelled.ItemDetail itemDetail = new OrderCancelled.ItemDetail(
                ProductId.generate(),
                Quantity.from(1)
        );
        return new OrderCancelled(
                OrderId.generate(),
                new OrderCancelledItems(List.of(itemDetail))
        );
    }
}
