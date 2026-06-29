package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Order;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.OrderLine;
import com.dochiri.tooltestlab.order.domain.model.OrderLines;
import com.dochiri.tooltestlab.order.domain.model.OrderStatus;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.order.domain.model.ShippingAddress;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.shared.model.Money;

import java.util.List;

final class OrderMapper {

    private OrderMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    static Order toDomain(final OrderEntity entity, final List<OrderLineEntity> itemEntities) {
        final List<OrderLine> domainItems = itemEntities.stream()
                .map(e -> new OrderLine(new ProductId(e.getProductPublicId()), new ProductName(e.getProductName()), new Quantity(e.getQuantity()), Money.of(e.getUnitPrice())))
                .toList();

        final ShippingAddress shippingAddress = new ShippingAddress(
                entity.getRecipientName(), entity.getPhone(), entity.getAddress(), entity.getZipCode());

        return new Order(
                new OrderId(entity.getPublicId()), new MemberId(entity.getMemberPublicId()), OrderLines.from(domainItems),
                OrderStatus.valueOf(entity.getStatus()), shippingAddress, Money.of(entity.getTotalAmount()));
    }

    static OrderEntity toEntity(final Order domain) {
        return OrderEntity.from(domain);
    }

    static List<OrderLineEntity> toItemEntities(final Order domain, final OrderEntity orderEntity) {
        return domain.items().values().stream()
                .map(i -> new OrderLineEntity(null, orderEntity.getId(), i.productId().value(), i.productName().value(), i.quantity().value(), i.unitPrice().amount()))
                .toList();
    }
}
