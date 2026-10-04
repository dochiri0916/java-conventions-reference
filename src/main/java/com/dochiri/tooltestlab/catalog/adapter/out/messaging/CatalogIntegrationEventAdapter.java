package com.dochiri.tooltestlab.catalog.adapter.out.messaging;

import com.dochiri.tooltestlab.catalog.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.catalog.application.port.out.event.CatalogIntegrationEvent;
import com.dochiri.tooltestlab.catalog.adapter.out.persistence.outbox.CatalogOutboxEntry;
import com.dochiri.tooltestlab.global.messaging.OutboxRelay;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxDispatcher;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class CatalogIntegrationEventAdapter implements IntegrationEventPort, OutboxRelay {
    private final OutboxStore store;
    private final OutboxDispatcher dispatcher;

    @Override
    public void publish(final CatalogIntegrationEvent event) {
        store.append(event, CatalogOutboxEntry::new);
    }

    @Override
    public int relay() {
        return dispatcher.dispatch(CatalogOutboxEntry.class, "catalog");
    }
}
