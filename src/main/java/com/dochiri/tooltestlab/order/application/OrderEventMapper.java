package com.dochiri.tooltestlab.order.application;

import com.dochiri.tooltestlab.order.application.port.out.event.OrderCancelledIntegrationEvent;
import com.dochiri.tooltestlab.order.application.port.out.event.OrderPlacedIntegrationEvent;
import com.dochiri.tooltestlab.order.domain.event.OrderCancelled;
import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;

public final class OrderEventMapper {
    private OrderEventMapper() {
    }

    public static OrderPlacedIntegrationEvent toIntegration(final OrderPlaced event) {
        final var order = event.order();
        return new OrderPlacedIntegrationEvent(order.id().value(), order.memberId().value(),
                order.cartId().value(), order.cartRevision(),
                order.items().values().stream().map(item -> new OrderPlacedIntegrationEvent.Item(
                        item.productId().value(), item.productName().value(),
                        item.quantity().value(), item.unitPrice().amount())).toList());
    }

    public static OrderCancelledIntegrationEvent toIntegration(final OrderCancelled event) {
        final var order = event.order();
        return new OrderCancelledIntegrationEvent(order.id()
                .value(),
                order.memberId()
                        .value(),
                order.cartId()
                        .value(),
                order.cartRevision());
    }

}
