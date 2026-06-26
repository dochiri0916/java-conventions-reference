package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.*;

import java.util.List;

/**
 * OrderEntity ↔ Order 변환 유틸리티.
 */
final class OrderMapper {

    private OrderMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    static Order toDomain(OrderEntity entity, List<OrderLineEntity> itemEntities) {
        List<OrderLine> domainItems = itemEntities.stream()
                .map(e -> new OrderLine(e.getProductPublicId(), e.getProductName(), e.getQuantity(), e.getUnitPrice()))
                .toList();

        ShippingAddress shippingAddress = new ShippingAddress(
                entity.getRecipientName(), entity.getPhone(), entity.getAddress(), entity.getZipCode());

        return new Order(
                new OrderId(entity.getPublicId()), new MemberId(entity.getMemberPublicId()), OrderLines.from(domainItems),
                OrderStatus.valueOf(entity.getStatus()), shippingAddress, Money.of(entity.getTotalAmount()));
    }

    static OrderEntity toEntity(Order domain) {
        return OrderEntity.from(domain);
    }

    static List<OrderLineEntity> toItemEntities(Order domain, OrderEntity orderEntity) {
        return domain.items().values().stream()
                .map(i -> new OrderLineEntity(null, orderEntity.getId(), i.productPublicId(), i.productName(), i.quantity(), i.unitPrice()))
                .toList();
    }
}
