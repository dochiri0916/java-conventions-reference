package com.dochiri.tooltestlab.order.adapter.out.messaging;

import com.dochiri.tooltestlab.order.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.order.application.port.out.event.OrderIntegrationEvent;
import com.dochiri.tooltestlab.order.adapter.out.persistence.outbox.OrderOutboxEntry;
import com.dochiri.tooltestlab.order.domain.outbox.OrderOutbox;
import com.dochiri.tooltestlab.global.messaging.OutboxRelay;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxDispatcher;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class OrderIntegrationEventAdapter implements IntegrationEventPort, OutboxRelay {
    private final OutboxStore store;
    private final OutboxDispatcher dispatcher;

    @Override
    public void publish(final OrderIntegrationEvent event) {
        store.append(event, message -> new OrderOutboxEntry(new OrderOutbox(message)));
    }

    @Override
    public int relay() {
        return dispatcher.dispatch(
                OrderOutboxEntry.class);
    }
}
