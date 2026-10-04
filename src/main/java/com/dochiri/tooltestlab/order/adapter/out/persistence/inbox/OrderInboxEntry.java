package com.dochiri.tooltestlab.order.adapter.out.persistence.inbox;

import com.dochiri.tooltestlab.global.messaging.persistence.InboxEntry;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "order_inbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderInboxEntry extends InboxEntry {
    public OrderInboxEntry(final String eventId, final String eventType, final Instant processedAt) {
        super(eventId, eventType, processedAt);
    }
}
