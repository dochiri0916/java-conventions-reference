package com.dochiri.tooltestlab.cart.application;

import com.dochiri.tooltestlab.cart.application.port.out.event.CartChangedIntegrationEvent;
import com.dochiri.tooltestlab.cart.domain.event.CartChanged;

public final class CartEventMapper {
    private CartEventMapper() {
    }

    public static CartChangedIntegrationEvent toIntegration(final CartChanged event) {
        final var cart = event.cart();
        return new CartChangedIntegrationEvent(cart.id().value(), cart.memberId().value(), cart.revision(),
                cart.items().values().stream().map(item -> new CartChangedIntegrationEvent.Item(
                        item.productId().value(), item.productName().value(),
                        item.quantity().value(), item.unitPrice().amount())).toList());
    }
}
