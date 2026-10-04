package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.exception.EmptyCartException;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderCommand;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderResult;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.CartSnapshotPort;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.order.application.OrderEventMapper;
import com.dochiri.tooltestlab.order.application.exception.CartSnapshotNotReadyException;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.vo.OrderLine;
import com.dochiri.tooltestlab.order.domain.vo.OrderLines;
import com.dochiri.tooltestlab.order.domain.vo.Phone;
import com.dochiri.tooltestlab.order.domain.vo.RecipientName;
import com.dochiri.tooltestlab.order.domain.vo.ShippingAddress;
import com.dochiri.tooltestlab.order.domain.vo.StreetAddress;
import com.dochiri.tooltestlab.order.domain.vo.ZipCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public final class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final CartSnapshotPort cartSnapshotPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public PlaceOrderResult place(final PlaceOrderCommand command) {
        final MemberReferenceId memberId = MemberReferenceId.parse(command.memberPublicId());
        final ShippingAddress shippingAddress = ShippingAddress.of(
                RecipientName.of(command.recipientName()), Phone.of(command.phone()),
                StreetAddress.of(command.address()), ZipCode.of(command.zipCode()));

        final var cart = cartSnapshotPort.claim(memberId).orElseThrow(CartSnapshotNotReadyException::new);
        if (cart.items().isEmpty()) {
            throw new EmptyCartException();
        }

        final Order saved = orderRepositoryPort.create(
                Order.create(memberId, cart.cartId(), cart.revision(), OrderLines.of(cart.items()), shippingAddress));
        integrationEventPort.publish(OrderEventMapper.toIntegration(saved.placedEvent()));

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
