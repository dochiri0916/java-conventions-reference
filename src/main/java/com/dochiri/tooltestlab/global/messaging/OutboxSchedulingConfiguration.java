package com.dochiri.tooltestlab.global.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(name = "messaging.outbox.enabled", havingValue = "true", matchIfMissing = true)
public class OutboxSchedulingConfiguration {
    private final List<OutboxRelay> relays;

    @Scheduled(fixedDelayString = "${messaging.outbox.poll-interval-ms:250}")
    public void relay() {
        relays.forEach(OutboxRelay::relay);
    }
}
