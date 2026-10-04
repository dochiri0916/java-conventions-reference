package com.dochiri.tooltestlab.order.application;

import com.dochiri.tooltestlab.order.application.port.out.event.*;
import com.dochiri.tooltestlab.order.domain.event.*;

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

    public static OrderConfirmedIntegrationEvent toIntegration(final OrderConfirmed event) {
        final var order = event.order();
        return new OrderConfirmedIntegrationEvent(order.id().value(), order.memberId().value(),
                order.cartId().value(), order.cartRevision());
    }

    public static OrderCancelledIntegrationEvent toIntegration(final OrderCancelled event) {
        final var order = event.order();
        return new OrderCancelledIntegrationEvent(order.id().value(), order.memberId().value(),
                order.cartId().value(), order.cartRevision());
    }

    public static OrderRejectedIntegrationEvent toIntegration(final OrderRejected event) {
        return new OrderRejectedIntegrationEvent(event.orderId().value(), event.reason());
    }
}
