package com.dochiri.tooltestlab.cart.adapter.out.persistence.inbox;

import com.dochiri.tooltestlab.global.messaging.persistence.InboxEntry;
import com.dochiri.tooltestlab.global.messaging.persistence.InboxProcessor;
import com.dochiri.tooltestlab.global.messaging.persistence.InboxRegistration;
import org.springframework.stereotype.Component;

@Component
final class CartInboxRegistration implements InboxRegistration {

    @Override
    public String consumer() {
        return "cart";
    }

    @Override
    public Class<? extends InboxEntry> type() {
        return CartInboxEntry.class;
    }

    @Override
    public InboxProcessor.EntryFactory factory() {
        return CartInboxEntry::new;
    }
}
