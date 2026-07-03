package com.dochiri.tooltestlab.order.adapter.out.event;

import com.dochiri.tooltestlab.order.application.port.out.OrderEventPort;
import com.dochiri.tooltestlab.order.domain.event.OrderCancelled;
import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@RequiredArgsConstructor
public final class SpringOrderEventAdapter implements OrderEventPort {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishAfterCommit(final OrderPlaced event) {
        publishAfterCommit((Object) event);
    }

    @Override
    public void publishAfterCommit(final OrderCancelled event) {
        publishAfterCommit((Object) event);
    }

    private void publishAfterCommit(final Object event) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    eventPublisher.publishEvent(event);
                }
            });
            return;
        }
        eventPublisher.publishEvent(event);
    }
}
