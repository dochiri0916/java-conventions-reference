package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.CartReferenceId;
import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.vo.OrderId;
import com.dochiri.tooltestlab.order.domain.vo.OrderLine;
import com.dochiri.tooltestlab.order.domain.vo.OrderLines;
import com.dochiri.tooltestlab.order.domain.vo.OrderStatus;
import com.dochiri.tooltestlab.order.domain.vo.Phone;
import com.dochiri.tooltestlab.order.domain.vo.ProductName;
import com.dochiri.tooltestlab.order.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.RecipientName;
import com.dochiri.tooltestlab.order.domain.vo.ShippingAddress;
import com.dochiri.tooltestlab.order.domain.vo.StreetAddress;
import com.dochiri.tooltestlab.order.domain.vo.ZipCode;

import java.util.List;
import com.github.f4b6a3.uuid.UuidCreator;

final class OrderMapper {

    private OrderMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    static Order toDomain(final OrderEntity entity, final List<OrderLineEntity> itemEntities) {
        final List<OrderLine> domainItems = itemEntities.stream()
                .map(e -> OrderLine.of(
                        ProductReferenceId.parse(e.getProductId()), ProductName.of(e.getProductName()),
                        Quantity.of(e.getQuantity()), Money.of(e.getUnitPrice())))
                .toList();

        final ShippingAddress shippingAddress = ShippingAddress.of(
                RecipientName.of(
                        entity.getRecipientName()),
                Phone.of(
                        entity.getPhone()),
                StreetAddress.of(
                        entity.getAddress()),
                ZipCode.of(
                        entity.getZipCode()));

        return Order.restore(
                OrderId.parse(
                        entity.getPublicId()),
                MemberReferenceId.parse(
                        entity.getMemberId()),
                CartReferenceId.parse(
                        entity.getCartId()),
                entity.getCartRevision(),
                OrderLines.of(domainItems),
                OrderStatus.valueOf(
                        entity.getStatus()), shippingAddress,
                Money.of(
                        entity.getTotalAmount()));
    }

    static OrderEntity toEntity(final Order domain) {
        final OrderEntity entity = OrderEntity.create(
                domain.id()
                        .value(),
                domain.memberId()
                        .value(),
                domain.status()
                        .name(),
                domain.totalAmount()
                        .amount(),
                domain.shippingAddress()
                        .recipientName()
                        .value(),
                domain.shippingAddress()
                        .phone()
                        .value(),
                domain.shippingAddress()
                        .address()
                        .value(),
                domain.shippingAddress()
                        .zipCode()
                        .value());
        entity.setCheckout(
                domain.cartId()
                        .value(),
                domain.cartRevision());
        return entity;
    }

    static void updateEntity(final OrderEntity entity, final Order domain) {
        entity.update(
                domain.status()
                        .name(),
                domain.totalAmount()
                        .amount(),
                domain.shippingAddress()
                        .recipientName()
                        .value(),
                domain.shippingAddress()
                        .phone()
                        .value(),
                domain.shippingAddress()
                        .address()
                        .value(),
                domain.shippingAddress()
                        .zipCode()
                        .value());
    }

    static List<OrderLineEntity> toItemEntities(final Order domain, final String orderId) {
        return domain.items().values().stream()
                .map(i -> OrderLineEntity.create(
                        UuidCreator.getTimeOrderedEpoch().toString(),
                        orderId,
                        i.productId().value(),
                        i.productName().value(),
                        i.quantity().value(),
                        i.unitPrice().amount()))
                .toList();
    }
}
