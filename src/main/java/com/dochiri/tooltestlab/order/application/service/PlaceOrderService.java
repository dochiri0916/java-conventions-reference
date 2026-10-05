package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.exception.EmptyCartException;
import com.dochiri.tooltestlab.order.application.port.in.command.PlaceOrderCommand;
import com.dochiri.tooltestlab.order.application.port.in.result.PlaceOrderResult;
import com.dochiri.tooltestlab.order.application.port.in.usecase.PlaceOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.CartSnapshotPort;
import com.dochiri.tooltestlab.order.application.port.out.CartSnapshotPort.CartSnapshot;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.order.application.OrderEventMapper;
import com.dochiri.tooltestlab.order.application.OrderResultMapper;
import com.dochiri.tooltestlab.order.application.exception.CartSnapshotNotReadyException;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.Order;
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
        final ShippingAddress shippingAddress = toShippingAddress(command);

        final CartSnapshot cart = claimNotEmptyCart(memberId);
        final Order saved = orderRepositoryPort.create(
                Order.create(memberId, cart.cartId(), cart.revision(), OrderLines.of(cart.items()), shippingAddress));
        integrationEventPort.publish(OrderEventMapper.toIntegration(saved.placedEvent()));

        return OrderResultMapper.toPlaceOrderResult(saved);
    }

    private CartSnapshot claimNotEmptyCart(final MemberReferenceId memberId) {
        final CartSnapshot cart = cartSnapshotPort.claim(memberId).orElseThrow(CartSnapshotNotReadyException::new);
        if (cart.items().isEmpty()) {
            throw new EmptyCartException();
        }
        return cart;
    }

    private static ShippingAddress toShippingAddress(final PlaceOrderCommand command) {
        return ShippingAddress.of(
                RecipientName.of(command.recipientName()), Phone.of(command.phone()),
                StreetAddress.of(command.address()), ZipCode.of(command.zipCode()));
    }
}
