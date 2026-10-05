package com.dochiri.tooltestlab.order.adapter.out.persistence.inbox;

import com.dochiri.tooltestlab.global.messaging.persistence.InboxEntry;
import com.dochiri.tooltestlab.global.messaging.persistence.InboxProcessor;
import com.dochiri.tooltestlab.global.messaging.persistence.InboxRegistration;
import org.springframework.stereotype.Component;

@Component
final class OrderInboxRegistration implements InboxRegistration {

    @Override
    public String consumer() {
        return "order";
    }

    @Override
    public Class<? extends InboxEntry> type() {
        return OrderInboxEntry.class;
    }

    @Override
    public InboxProcessor.EntryFactory factory() {
        return OrderInboxEntry::new;
    }
}
