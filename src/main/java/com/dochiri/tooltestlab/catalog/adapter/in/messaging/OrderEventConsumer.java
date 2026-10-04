package com.dochiri.tooltestlab.catalog.adapter.in.messaging;

import com.dochiri.tooltestlab.catalog.adapter.out.persistence.inbox.CatalogInboxEntry;
import com.dochiri.tooltestlab.catalog.application.port.in.*;
import com.dochiri.tooltestlab.global.messaging.EventCodec;
import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import com.dochiri.tooltestlab.global.messaging.persistence.InboxProcessor;
import com.dochiri.tooltestlab.order.application.port.out.event.OrderPlacedIntegrationEvent;
import com.dochiri.tooltestlab.order.application.port.out.event.OrderCancelledIntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class OrderEventConsumer {
    private final ReserveOrderStockUseCase reserveOrderStockUseCase;
    private final ReleaseOrderStockUseCase releaseOrderStockUseCase;
    private final InboxProcessor inbox;
    private final EventCodec codec;

    @EventListener
    public void receive(final MessageEnvelope message) {
        if (!"order".equals(message.source())) {
            return;
        }
        switch (message.eventType()) {
            case OrderPlacedIntegrationEvent.TYPE -> inbox.process(message, CatalogInboxEntry.class, CatalogInboxEntry::new, () -> {
                final var event = codec.decode(message.payload(), OrderPlacedIntegrationEvent.class);
                reserveOrderStockUseCase.reserve(new ReserveOrderStockCommand(event.orderId(), event.items().stream()
                        .map(item -> new ReserveOrderStockCommand.Item(item.productId(), item.quantity())).toList()));
            });
            case OrderCancelledIntegrationEvent.TYPE -> inbox.process(message, CatalogInboxEntry.class, CatalogInboxEntry::new, () -> {
                final var event = codec.decode(message.payload(), OrderCancelledIntegrationEvent.class);
                releaseOrderStockUseCase.release(new ReleaseOrderStockCommand(event.orderId()));
            });
            default -> { }
        }
    }
}
