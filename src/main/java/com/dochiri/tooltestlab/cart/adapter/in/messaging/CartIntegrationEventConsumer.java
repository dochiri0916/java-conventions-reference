package com.dochiri.tooltestlab.cart.adapter.in.messaging;

import com.dochiri.tooltestlab.cart.adapter.out.persistence.inbox.CartInboxEntry;
import com.dochiri.tooltestlab.cart.application.port.in.*;
import com.dochiri.tooltestlab.catalog.application.port.out.event.ProductChangedIntegrationEvent;
import com.dochiri.tooltestlab.order.application.port.out.event.OrderConfirmedIntegrationEvent;
import com.dochiri.tooltestlab.global.messaging.EventCodec;
import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import com.dochiri.tooltestlab.global.messaging.persistence.InboxProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class CartIntegrationEventConsumer {
    private final UpdateProductSnapshotUseCase updateProductSnapshotUseCase;
    private final CompleteCartCheckoutUseCase completeCartCheckoutUseCase;
    private final InboxProcessor inbox;
    private final EventCodec codec;

    @EventListener
    public void receive(final MessageEnvelope message) {
        if ("catalog".equals(message.source()) && ProductChangedIntegrationEvent.TYPE.equals(message.eventType())) {
            inbox.process(message, CartInboxEntry.class, CartInboxEntry::new, () -> {
                final var event = codec.decode(message.payload(), ProductChangedIntegrationEvent.class);
                updateProductSnapshotUseCase.update(new UpdateProductSnapshotCommand(
                        event.productId(), event.name(), event.priceAmount(), message.sequence()));
            });
        } else if ("order".equals(message.source()) && OrderConfirmedIntegrationEvent.TYPE.equals(message.eventType())) {
            inbox.process(message, CartInboxEntry.class, CartInboxEntry::new, () -> {
                final var event = codec.decode(message.payload(), OrderConfirmedIntegrationEvent.class);
                completeCartCheckoutUseCase.complete(new CompleteCartCheckoutCommand(
                        event.memberId(), event.cartId(), event.cartRevision()));
            });
        }
    }
}
