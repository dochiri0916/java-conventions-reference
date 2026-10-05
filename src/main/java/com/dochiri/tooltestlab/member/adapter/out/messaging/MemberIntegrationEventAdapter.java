package com.dochiri.tooltestlab.member.adapter.out.messaging;

import com.dochiri.tooltestlab.member.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.member.application.port.out.event.MemberIntegrationEvent;
import com.dochiri.tooltestlab.member.adapter.out.persistence.outbox.MemberOutboxEntry;
import com.dochiri.tooltestlab.member.domain.outbox.MemberOutbox;
import com.dochiri.tooltestlab.global.messaging.OutboxRelay;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxDispatcher;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class MemberIntegrationEventAdapter implements IntegrationEventPort, OutboxRelay {
    private final OutboxStore store;
    private final OutboxDispatcher dispatcher;

    @Override
    public void publish(final MemberIntegrationEvent event) {
        store.append(event, message -> new MemberOutboxEntry(new MemberOutbox(message)));
    }

    @Override
    public int relay() {
        return dispatcher.dispatch(
                MemberOutboxEntry.class);
    }
}
