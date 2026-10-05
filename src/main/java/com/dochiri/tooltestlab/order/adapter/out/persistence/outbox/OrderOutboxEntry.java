package com.dochiri.tooltestlab.order.adapter.out.persistence.outbox;

import com.dochiri.tooltestlab.global.messaging.persistence.OutboxEntry;
import com.dochiri.tooltestlab.order.domain.outbox.OrderOutbox;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_outbox", indexes = @Index(name = "idx_order_outbox_pending", columnList = "publishedAt,id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderOutboxEntry extends OutboxEntry {
    public OrderOutboxEntry(final OrderOutbox outbox) {
        super(outbox);
    }

    @Override
    public OrderOutbox toDomain() {
        return new OrderOutbox(message(), delivery());
    }
}
