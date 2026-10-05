package com.dochiri.tooltestlab.review.domain.outbox;

import com.dochiri.tooltestlab.global.domain.outbox.Outbox;

public final class ReviewOutbox extends Outbox {
    public ReviewOutbox(final Message message) {
        super(message);
    }

    public ReviewOutbox(final Message message, final Delivery delivery) {
        super(message, delivery);
    }

    @Override
    public String source() {
        return "review";
    }

    @Override
    protected ReviewOutbox withDelivery(final Delivery delivery) {
        return new ReviewOutbox(message(), delivery);
    }
}
