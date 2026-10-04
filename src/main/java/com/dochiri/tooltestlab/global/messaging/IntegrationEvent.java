package com.dochiri.tooltestlab.global.messaging;

/** 공개 계약은 문자열 ID와 원시 값만 전달한다. */
public interface IntegrationEvent {
    String eventType();
    String aggregateId();
}
