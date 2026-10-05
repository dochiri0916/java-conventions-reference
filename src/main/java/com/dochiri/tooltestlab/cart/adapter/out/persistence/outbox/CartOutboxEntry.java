package com.dochiri.tooltestlab.cart.adapter.out.persistence.outbox;

import com.dochiri.tooltestlab.global.messaging.persistence.OutboxEntry;
import com.dochiri.tooltestlab.cart.domain.outbox.CartOutbox;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_outbox", indexes = @Index(name = "idx_cart_outbox_pending", columnList = "publishedAt,id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartOutboxEntry extends OutboxEntry {
    public CartOutboxEntry(final CartOutbox outbox) {
        super(outbox);
    }

    @Override
    public CartOutbox toDomain() {
        return new CartOutbox(message(), delivery());
    }
}
