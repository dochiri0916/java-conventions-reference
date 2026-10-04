package com.dochiri.tooltestlab.cart.adapter.out.persistence.inbox;

import com.dochiri.tooltestlab.global.messaging.persistence.InboxEntry;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "cart_inbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartInboxEntry extends InboxEntry {
    public CartInboxEntry(final String eventId, final String eventType, final Instant processedAt) {
        super(eventId, eventType, processedAt);
    }
}
