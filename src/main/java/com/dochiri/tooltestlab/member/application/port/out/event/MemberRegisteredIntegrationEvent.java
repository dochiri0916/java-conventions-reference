package com.dochiri.tooltestlab.member.application.port.out.event;

public record MemberRegisteredIntegrationEvent(String memberId) implements MemberIntegrationEvent {
    public static final String TYPE = "member.member-registered.v1";

    @Override
    public String eventType() {
        return TYPE;
    }

    @Override
    public String aggregateId() {
        return memberId;
    }
}
