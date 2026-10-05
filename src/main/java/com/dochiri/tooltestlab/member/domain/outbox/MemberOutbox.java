package com.dochiri.tooltestlab.member.domain.outbox;

import com.dochiri.tooltestlab.global.domain.outbox.Outbox;

public final class MemberOutbox extends Outbox {
    public MemberOutbox(final Message message) {
        super(message);
    }

    public MemberOutbox(final Message message, final Delivery delivery) {
        super(message, delivery);
    }

    @Override
    public String source() {
        return "member";
    }

    @Override
    protected MemberOutbox withDelivery(final Delivery delivery) {
        return new MemberOutbox(message(), delivery);
    }
}
