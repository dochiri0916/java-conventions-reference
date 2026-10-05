package com.dochiri.tooltestlab.order.domain.outbox;

import com.dochiri.tooltestlab.global.domain.outbox.Outbox;

public final class OrderOutbox extends Outbox {
    public OrderOutbox(final Message message) {
        super(message);
    }

    public OrderOutbox(final Message message, final Delivery delivery) {
        super(message, delivery);
    }

    @Override
    public String source() {
        return "order";
    }

    @Override
    protected OrderOutbox withDelivery(final Delivery delivery) {
        return new OrderOutbox(message(), delivery);
    }
}
