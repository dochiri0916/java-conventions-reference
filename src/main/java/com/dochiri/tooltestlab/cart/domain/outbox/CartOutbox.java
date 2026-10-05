package com.dochiri.tooltestlab.cart.domain.outbox;

import com.dochiri.tooltestlab.global.domain.outbox.Outbox;

public final class CartOutbox extends Outbox {
    public CartOutbox(final Message message) {
        super(message);
    }

    public CartOutbox(final Message message, final Delivery delivery) {
        super(message, delivery);
    }

    @Override
    public String source() {
        return "cart";
    }

    @Override
    protected CartOutbox withDelivery(final Delivery delivery) {
        return new CartOutbox(message(), delivery);
    }
}
