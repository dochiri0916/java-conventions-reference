package com.dochiri.tooltestlab.cart.adapter.in.messaging;

import com.dochiri.tooltestlab.cart.application.port.in.command.CompleteCartCheckoutCommand;
import com.dochiri.tooltestlab.cart.application.port.in.command.UpdateProductSnapshotCommand;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.CompleteCartCheckoutUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.UpdateProductSnapshotUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.out.event.ProductChangedIntegrationEvent;
import com.dochiri.tooltestlab.global.messaging.EventCodec;
import com.dochiri.tooltestlab.global.messaging.MessageConsumer;
import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import com.dochiri.tooltestlab.global.messaging.persistence.InboxProcessor;
import com.dochiri.tooltestlab.order.application.port.out.event.OrderPlacedIntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class CartIntegrationEventConsumer implements MessageConsumer {
    private static final String CONSUMER = "cart";
    private static final String CATALOG_SOURCE = "catalog";
    private static final String ORDER_SOURCE = "order";
    private static final String PRODUCT_CHANGED = ProductChangedIntegrationEvent.TYPE;
    private static final String ORDER_PLACED = OrderPlacedIntegrationEvent.TYPE;

    private final UpdateProductSnapshotUseCase updateProductSnapshotUseCase;
    private final CompleteCartCheckoutUseCase completeCartCheckoutUseCase;
    private final InboxProcessor inbox;
    private final EventCodec codec;

    @Override
    public void receive(final MessageEnvelope message) {
        if (isFrom(message, CATALOG_SOURCE, PRODUCT_CHANGED)) {
            inbox.process(message, CONSUMER, () -> updateProductSnapshot(message));
            return;
        }
        if (isFrom(message, ORDER_SOURCE, ORDER_PLACED)) {
            inbox.process(message, CONSUMER, () -> completeCheckout(message));
        }
    }

    private static boolean isFrom(final MessageEnvelope message, final String source, final String eventType) {
        final String actualSource = message.source();
        final String actualType = message.eventType();
        return source.equals(actualSource) &&
                eventType.equals(actualType);
    }

    private void updateProductSnapshot(final MessageEnvelope message) {
        final String payload = message.payload();
        final long sequence = message.sequence();
        final var event = codec.decode(payload,
                ProductChangedIntegrationEvent.class);
        updateProductSnapshotUseCase.update(new UpdateProductSnapshotCommand(
                event.productId(),
                event.name(),
                event.priceAmount(),
                sequence));
    }

    private void completeCheckout(final MessageEnvelope message) {
        final String payload = message.payload();
        final var event = codec.decode(payload,
                OrderPlacedIntegrationEvent.class);
        completeCartCheckoutUseCase.complete(new CompleteCartCheckoutCommand(
                event.memberId(),
                event.cartId(),
                event.cartRevision()));
    }
}
