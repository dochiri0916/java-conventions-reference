package com.dochiri.tooltestlab.member.application.port.out.event;

import com.dochiri.tooltestlab.global.messaging.IntegrationEvent;

public sealed interface MemberIntegrationEvent extends IntegrationEvent
        permits MemberRegisteredIntegrationEvent {
}
