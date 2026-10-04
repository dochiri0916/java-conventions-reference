package com.dochiri.tooltestlab.global.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/** 단일 애플리케이션 배포용 전송 구현. 수신자의 트랜잭션은 발신자와 분리된다. */
@Component
@RequiredArgsConstructor
public final class LocalMessageBroker implements MessageBroker {
    private final ApplicationEventPublisher publisher;

    @Override
    public void send(final MessageEnvelope message) {
        publisher.publishEvent(message);
    }
}
