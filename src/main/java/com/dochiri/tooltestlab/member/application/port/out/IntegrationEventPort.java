package com.dochiri.tooltestlab.member.application.port.out;

import com.dochiri.tooltestlab.member.application.port.out.event.MemberIntegrationEvent;

@FunctionalInterface
public interface IntegrationEventPort {
    void publish(MemberIntegrationEvent event);
}
