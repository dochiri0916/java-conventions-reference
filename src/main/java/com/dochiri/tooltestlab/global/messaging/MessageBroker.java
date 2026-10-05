package com.dochiri.tooltestlab.global.messaging;

import java.util.concurrent.CompletionStage;

/** 완료는 전송 수단의 인수 확인을 뜻한다. 소비자의 비즈니스 처리 완료는 별도다. */
public interface MessageBroker {
    CompletionStage<Void> send(MessageEnvelope message);
}
