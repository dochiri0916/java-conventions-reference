package com.dochiri.tooltestlab.global.messaging;

/** 전송 어댑터는 이 계약을 호출하고, 정상 반환 뒤 수신을 확인한다. */
public interface MessageConsumer {
    void receive(MessageEnvelope message);
}
