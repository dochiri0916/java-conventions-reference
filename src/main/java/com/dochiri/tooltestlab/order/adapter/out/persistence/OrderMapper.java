package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.*;

import java.util.List;
import java.util.UUID;

final class OrderMapper {

    private OrderMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    static Order toDomain(final OrderEntity entity, final List<OrderLineEntity> itemEntities) {
        final List<OrderLine> domainItems = itemEntities.stream()
                .map(e -> new OrderLine(
                        new ProductId(e.getProductId()), new ProductName(e.getProductName()),
                        new Quantity(e.getQuantity()), new Money(e.getUnitPrice())))
                .toList();

        final ShippingAddress shippingAddress = new ShippingAddress(
                new RecipientName(entity.getRecipientName()), new Phone(entity.getPhone()),
                new StreetAddress(entity.getAddress()), new ZipCode(entity.getZipCode()));

        return Order.restore(
                new OrderId(entity.getOrderId()), new MemberId(entity.getMemberId()), OrderLines.from(domainItems),
                OrderStatus.valueOf(entity.getStatus()), shippingAddress, new Money(entity.getTotalAmount()));
    }

    static OrderEntity toEntity(final Order domain) {
        return OrderEntity.from(domain);
    }

    static List<OrderLineEntity> toItemEntities(final Order domain, final String orderId) {
        return domain.items().values().stream()
                .map(i -> OrderLineEntity.create(
                        UUID.randomUUID().toString().replace("-", ""),
                        orderId,
                        i.productId().value(),
                        i.productName().value(),
                        i.quantity().value(),
                        i.unitPrice().amount()))
                .toList();
    }
}
