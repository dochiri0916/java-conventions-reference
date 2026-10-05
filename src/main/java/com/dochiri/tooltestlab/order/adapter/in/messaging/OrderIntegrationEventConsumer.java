package com.dochiri.tooltestlab.order.adapter.in.messaging;

import com.dochiri.tooltestlab.cart.application.port.out.event.CartChangedIntegrationEvent;
import com.dochiri.tooltestlab.global.messaging.EventCodec;
import com.dochiri.tooltestlab.global.messaging.MessageConsumer;
import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import com.dochiri.tooltestlab.global.messaging.persistence.InboxProcessor;
import com.dochiri.tooltestlab.order.application.port.in.command.CartSnapshotItems;
import com.dochiri.tooltestlab.order.application.port.in.command.UpdateCartSnapshotCommand;
import com.dochiri.tooltestlab.order.application.port.in.usecase.UpdateCartSnapshotUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class OrderIntegrationEventConsumer implements MessageConsumer {
    private static final String CONSUMER = "order";
    private static final String CART_SOURCE = "cart";
    private static final String CART_CHANGED = CartChangedIntegrationEvent.TYPE;

    private final UpdateCartSnapshotUseCase updateCartSnapshotUseCase;
    private final InboxProcessor inbox;
    private final EventCodec codec;

    @Override
    public void receive(final MessageEnvelope message) {
        final String source = message.source();
        final String eventType = message.eventType();
        if (CART_SOURCE.equals(source) &&
                CART_CHANGED.equals(eventType)) {
            inbox.process(message, CONSUMER, () -> updateCartSnapshot(message));
        }
    }

    private void updateCartSnapshot(final MessageEnvelope message) {
        final String payload = message.payload();
        final long sequence = message.sequence();
        final var event = codec.decode(payload,
                CartChangedIntegrationEvent.class);
        final List<UpdateCartSnapshotCommand.Item> items = event.items().stream()
                .map(item -> new UpdateCartSnapshotCommand.Item(
                        item.productId(), item.productName(), item.quantity(), item.unitPrice()))
                .toList();
        updateCartSnapshotUseCase.update(new UpdateCartSnapshotCommand(
                event.cartId(),
                event.memberId(),
                event.revision(), sequence, new CartSnapshotItems(items)));
    }
}
