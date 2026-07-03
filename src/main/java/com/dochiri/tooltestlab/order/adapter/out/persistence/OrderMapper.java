package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Order;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.OrderLine;
import com.dochiri.tooltestlab.order.domain.model.OrderLines;
import com.dochiri.tooltestlab.order.domain.model.OrderStatus;
import com.dochiri.tooltestlab.order.domain.model.Phone;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.order.domain.model.RecipientName;
import com.dochiri.tooltestlab.order.domain.model.ShippingAddress;
import com.dochiri.tooltestlab.order.domain.model.StreetAddress;
import com.dochiri.tooltestlab.order.domain.model.ZipCode;
import com.dochiri.tooltestlab.product.domain.model.Money;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;

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
                        new Quantity(e.getQuantity()), Money.of(e.getUnitPrice())))
                .toList();

        final ShippingAddress shippingAddress = new ShippingAddress(
                new RecipientName(entity.getRecipientName()), new Phone(entity.getPhone()),
                new StreetAddress(entity.getAddress()), new ZipCode(entity.getZipCode()));

        return new Order(
                new OrderId(entity.getOrderId()), new MemberId(entity.getMemberId()), OrderLines.from(domainItems),
                OrderStatus.valueOf(entity.getStatus()), shippingAddress, Money.of(entity.getTotalAmount()));
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
