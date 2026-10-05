package com.dochiri.tooltestlab.global.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "messaging.transport", havingValue = "local", matchIfMissing = true)
public final class LocalMessageReceiver {
    private final List<MessageConsumer> consumers;

    @EventListener
    public void receive(final MessageEnvelope message) {
        consumers.forEach(consumer -> consumer.receive(message));
    }
}
