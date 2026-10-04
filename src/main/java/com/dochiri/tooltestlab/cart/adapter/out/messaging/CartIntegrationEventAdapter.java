package com.dochiri.tooltestlab.cart.adapter.out.messaging;

import com.dochiri.tooltestlab.cart.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.cart.application.port.out.event.CartIntegrationEvent;
import com.dochiri.tooltestlab.cart.adapter.out.persistence.outbox.CartOutboxEntry;
import com.dochiri.tooltestlab.global.messaging.OutboxRelay;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxDispatcher;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class CartIntegrationEventAdapter implements IntegrationEventPort, OutboxRelay {
    private final OutboxStore store;
    private final OutboxDispatcher dispatcher;

    @Override
    public void publish(final CartIntegrationEvent event) {
        store.append(event, CartOutboxEntry::new);
    }

    @Override
    public int relay() {
        return dispatcher.dispatch(CartOutboxEntry.class, "cart");
    }
}
