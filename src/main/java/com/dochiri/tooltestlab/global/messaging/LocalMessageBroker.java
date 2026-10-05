package com.dochiri.tooltestlab.global.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/** 단일 애플리케이션 배포용 전송 구현. 수신자의 트랜잭션은 발신자와 분리된다. */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "messaging.transport", havingValue = "local", matchIfMissing = true)
public final class LocalMessageBroker implements MessageBroker {
    private final ApplicationEventPublisher publisher;

    @Override
    public CompletionStage<Void> send(final MessageEnvelope message) {
        publisher.publishEvent(message);
        return CompletableFuture.completedFuture(null);
    }
}
