package com.dochiri.tooltestlab.order.adapter.in.messaging;

import com.dochiri.tooltestlab.order.adapter.out.persistence.inbox.OrderInboxEntry;
import com.dochiri.tooltestlab.order.application.port.in.*;
import com.dochiri.tooltestlab.cart.application.port.out.event.CartChangedIntegrationEvent;
import com.dochiri.tooltestlab.catalog.application.port.out.event.StockReservedIntegrationEvent;
import com.dochiri.tooltestlab.catalog.application.port.out.event.StockRejectedIntegrationEvent;
import com.dochiri.tooltestlab.global.messaging.EventCodec;
import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import com.dochiri.tooltestlab.global.messaging.persistence.InboxProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class OrderIntegrationEventConsumer {
    private final UpdateCartSnapshotUseCase updateCartSnapshotUseCase;
    private final HandleStockReservationResultUseCase handleStockReservationResultUseCase;
    private final InboxProcessor inbox;
    private final EventCodec codec;

    @EventListener
    public void receive(final MessageEnvelope message) {
        if ("cart".equals(message.source()) && CartChangedIntegrationEvent.TYPE.equals(message.eventType())) {
            inbox.process(message, OrderInboxEntry.class, OrderInboxEntry::new, () -> {
                final var event = codec.decode(message.payload(), CartChangedIntegrationEvent.class);
                updateCartSnapshotUseCase.update(new UpdateCartSnapshotCommand(
                        event.cartId(), event.memberId(), event.revision(), message.sequence(), event.items().stream()
                        .map(item -> new UpdateCartSnapshotCommand.Item(
                                item.productId(), item.productName(), item.quantity(), item.unitPrice())).toList()));
            });
        } else if ("catalog".equals(message.source())) {
            switch (message.eventType()) {
                case StockReservedIntegrationEvent.TYPE -> inbox.process(message, OrderInboxEntry.class, OrderInboxEntry::new, () -> {
                    final var event = codec.decode(message.payload(), StockReservedIntegrationEvent.class);
                    handleStockReservationResultUseCase.handle(new HandleStockReservationResultCommand(event.orderId(), true, null));
                });
                case StockRejectedIntegrationEvent.TYPE -> inbox.process(message, OrderInboxEntry.class, OrderInboxEntry::new, () -> {
                    final var event = codec.decode(message.payload(), StockRejectedIntegrationEvent.class);
                    handleStockReservationResultUseCase.handle(new HandleStockReservationResultCommand(event.orderId(), false, event.reason()));
                });
                default -> { }
            }
        }
    }
}
