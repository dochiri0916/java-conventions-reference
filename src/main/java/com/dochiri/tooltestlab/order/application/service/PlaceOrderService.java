package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.order.application.exception.EmptyCartException;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderCommand;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderResult;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderEventPort;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;
import com.dochiri.tooltestlab.order.domain.model.OrderPlacedItems;
import com.dochiri.tooltestlab.order.domain.model.Order;
import com.dochiri.tooltestlab.order.domain.model.OrderLine;
import com.dochiri.tooltestlab.order.domain.model.OrderLines;
import com.dochiri.tooltestlab.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.exception.InsufficientStockException;
import com.dochiri.tooltestlab.product.domain.model.Product;
import com.dochiri.tooltestlab.product.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public final class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final OrderEventPort orderEventPort;

    @Override
    @Transactional
    public PlaceOrderResult place(final PlaceOrderCommand command) {
        final Cart cart = cartRepositoryPort.findByMemberId(command.memberId().value())
                .orElseThrow(EmptyCartException::emptyCart);

        if (cart.isEmpty()) {
            throw EmptyCartException.emptyCart();
        }

        final List<OrderLine> orderLines = new ArrayList<>();
        for (var cartItem : cart.items().values()) {
            final Product product = productRepositoryPort.findByPublicId(cartItem.productId().value())
                    .orElseThrow(() -> ProductNotFoundException.productNotFound(cartItem.productId().value()));

            if (product.stock().value() < cartItem.quantity().value()) {
                throw InsufficientStockException.lowStock(product.stock().value(), cartItem.quantity().value());
            }

            orderLines.add(new OrderLine(
                    cartItem.productId(), cartItem.productName(),
                    cartItem.quantity(), Money.of(cartItem.unitPrice().amount())));
        }

        Order order = Order.create(command.memberId(), OrderLines.from(orderLines), command.shippingAddress());
        Order saved = orderRepositoryPort.save(order);

        List<OrderPlaced.ItemDetail> itemDetails = orderLines.stream()
                .map(ol -> new OrderPlaced.ItemDetail(ol.productId(), ol.productName(), ol.quantity(), ol.unitPrice()))
                .toList();
        orderEventPort.publishAfterCommit(new OrderPlaced(
                saved.id(),
                command.memberId(),
                new OrderPlacedItems(itemDetails)));

        final var resultItems = saved.items().values().stream()
                .map(i -> new PlaceOrderResult.OrderItemResult(
                        i.productId().value(),
                        i.productName().value(),
                        i.quantity().value(),
                        i.unitPrice().amount()))
                .toList();
        return new PlaceOrderResult(
                saved.id().value(),
                saved.status().name(),
                saved.totalAmount().amount(),
                resultItems);
    }
}
