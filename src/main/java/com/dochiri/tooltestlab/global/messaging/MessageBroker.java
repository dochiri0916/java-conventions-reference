package com.dochiri.tooltestlab.global.messaging;

/** 정상 반환은 모든 수신자의 처리가 완료되었음을 의미한다. */
public interface MessageBroker {
    void send(MessageEnvelope message);
}
