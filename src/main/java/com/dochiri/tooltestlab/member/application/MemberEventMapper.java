package com.dochiri.tooltestlab.member.application;

import com.dochiri.tooltestlab.member.application.port.out.event.MemberRegisteredIntegrationEvent;
import com.dochiri.tooltestlab.member.domain.event.MemberRegistered;

public final class MemberEventMapper {
    private MemberEventMapper() {
    }

    public static MemberRegisteredIntegrationEvent toIntegration(final MemberRegistered event) {
        return new MemberRegisteredIntegrationEvent(event.memberId().value());
    }
}
