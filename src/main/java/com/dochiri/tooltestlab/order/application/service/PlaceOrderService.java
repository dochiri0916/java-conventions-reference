package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.order.application.command.PlaceOrderCommand;
import com.dochiri.tooltestlab.order.application.port.in.CancelOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.in.GetOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.in.ListOrdersUseCase;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.application.result.GetOrderResult;
import com.dochiri.tooltestlab.order.application.result.ListOrdersResult;
import com.dochiri.tooltestlab.order.application.result.PlaceOrderResult;
import com.dochiri.tooltestlab.order.domain.event.OrderCancelledEvent;
import com.dochiri.tooltestlab.order.domain.event.OrderPlacedEvent;
import com.dochiri.tooltestlab.order.domain.model.Order;
import com.dochiri.tooltestlab.order.domain.model.OrderLine;
import com.dochiri.tooltestlab.order.domain.model.OrderLines;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PlaceOrderService implements PlaceOrderUseCase, GetOrderUseCase, ListOrdersUseCase, CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final ApplicationEventPublisher eventPublisher;

    public PlaceOrderService(OrderRepositoryPort orderRepositoryPort,
                             CartRepositoryPort cartRepositoryPort,
                             ProductRepositoryPort productRepositoryPort,
                             ApplicationEventPublisher eventPublisher) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.cartRepositoryPort = cartRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public PlaceOrderResult place(PlaceOrderCommand command) {
        // 장바구니 조회
        Cart cart = cartRepositoryPort.findByMemberId(command.memberId().value())
                .orElseThrow(() -> new IllegalStateException("장바구니가 비어 있습니다."));

        if (cart.isEmpty()) {
            throw new IllegalStateException("장바구니가 비어 있습니다.");
        }

        // 재고 확인 (read-only — 실제 차감은 이벤트 핸들러에서)
        List<OrderLine> orderLines = new ArrayList<>();
        for (var cartItem : cart.items().values()) {
            Product product = productRepositoryPort.findByPublicId(cartItem.productPublicId())
                    .orElseThrow(() -> new IllegalStateException(
                            "상품을 찾을 수 없습니다. productPublicId=" + cartItem.productPublicId()));

            if (product.stock() < cartItem.quantity()) {
                throw new IllegalStateException(
                        "재고가 부족합니다. productPublicId=" + cartItem.productPublicId() +
                        ", 현재 재고: " + product.stock() + ", 요청 수량: " + cartItem.quantity());
            }

            orderLines.add(new OrderLine(
                    cartItem.productPublicId(), cartItem.productName(),
                    cartItem.quantity(), cartItem.unitPrice().amount()));
        }

        // 주문 생성 (Primary Aggregate)
        Order order = Order.create(command.memberId(), OrderLines.from(orderLines), command.shippingAddress());
        Order saved = orderRepositoryPort.save(order);

        // 이벤트 발행 → 재고 차감 & 장바구니 비우기는 별도 트랜잭션에서 처리
        List<OrderPlacedEvent.ItemDetail> itemDetails = orderLines.stream()
                .map(ol -> new OrderPlacedEvent.ItemDetail(ol.productPublicId(), ol.productName(), ol.quantity(), ol.unitPrice()))
                .toList();
        eventPublisher.publishEvent(new OrderPlacedEvent(saved.id(), command.memberId(), itemDetails));

        // 결과
        var resultItems = saved.items().values().stream()
                .map(i -> new PlaceOrderResult.OrderItemResult(i.productPublicId(), i.productName(), i.quantity(), i.unitPrice()))
                .toList();
        return new PlaceOrderResult(saved.id().value(), saved.status().name(), saved.totalAmount().amount(), resultItems);
    }

    @Override
    @Transactional(readOnly = true)
    public GetOrderResult getOrder(String publicId) {
        Order order = orderRepositoryPort.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. publicId=" + publicId));

        var items = order.items().values().stream()
                .map(i -> new GetOrderResult.Item(i.productPublicId(), i.productName(), i.quantity(), i.unitPrice(), i.totalPrice()))
                .toList();

        return new GetOrderResult(
                order.id().value(), order.memberId().value(), order.status().name(), order.totalAmount().amount(),
                order.shippingAddress().recipientName(), order.shippingAddress().phone(),
                order.shippingAddress().address(), order.shippingAddress().zipCode(),
                items);
    }

    @Override
    @Transactional(readOnly = true)
    public ListOrdersResult listByMember(String memberPublicId) {
        List<Order> orders = orderRepositoryPort.findByMemberId(memberPublicId);
        var summaries = orders.stream()
                .map(o -> new ListOrdersResult.OrderSummary(o.id().value(), o.status().name(), o.totalAmount().amount(), o.items().size()))
                .toList();
        return new ListOrdersResult(summaries);
    }

    @Override
    public void cancel(String memberPublicId, String orderPublicId) {
        Order order = orderRepositoryPort.findByPublicId(orderPublicId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. publicId=" + orderPublicId));

        if (!order.memberId().value().equals(memberPublicId)) {
            throw new IllegalArgumentException("자신의 주문만 취소할 수 있습니다.");
        }

        Order cancelled = order.cancel();
        orderRepositoryPort.save(cancelled);

        // 이벤트 발행 → 재고 복구는 별도 트랜잭션에서 처리
        List<OrderCancelledEvent.ItemDetail> itemDetails = cancelled.items().values().stream()
                .map(i -> new OrderCancelledEvent.ItemDetail(i.productPublicId(), i.quantity()))
                .toList();
        eventPublisher.publishEvent(new OrderCancelledEvent(cancelled.id(), itemDetails));
    }
}
