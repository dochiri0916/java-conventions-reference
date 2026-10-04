package com.dochiri.tooltestlab.review.adapter.out.messaging;

import com.dochiri.tooltestlab.review.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.review.application.port.out.event.ReviewIntegrationEvent;
import com.dochiri.tooltestlab.review.adapter.out.persistence.outbox.ReviewOutboxEntry;
import com.dochiri.tooltestlab.global.messaging.OutboxRelay;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxDispatcher;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReviewIntegrationEventAdapter implements IntegrationEventPort, OutboxRelay {
    private final OutboxStore store;
    private final OutboxDispatcher dispatcher;

    @Override
    public void publish(final ReviewIntegrationEvent event) {
        store.append(event, ReviewOutboxEntry::new);
    }

    @Override
    public int relay() {
        return dispatcher.dispatch(ReviewOutboxEntry.class, "review");
    }
}
