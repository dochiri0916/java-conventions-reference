package com.dochiri.tooltestlab.member.application.port.out;

import com.dochiri.tooltestlab.member.application.port.out.event.MemberIntegrationEvent;

public interface IntegrationEventPort {
    void publish(MemberIntegrationEvent event);
}
