package com.dochiri.tooltestlab.global.messaging;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(OutboxSchedulingConfiguration.class);
    private final List<OutboxRelay> relays;

    @Scheduled(fixedDelayString = "${messaging.outbox.poll-interval-ms:250}")
    public void relay() {
        for (final var relay : relays) {
            try {
                relay.relay();
            } catch (final RuntimeException failure) {
                LOG.warn("Outbox 릴레이 실패. relay={}", relay.getClass().getSimpleName(), failure);
            }
        }
    }
}
