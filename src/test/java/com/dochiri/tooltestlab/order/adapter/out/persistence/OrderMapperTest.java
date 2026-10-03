package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.OrderStatus;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.OrderLine;
import com.dochiri.tooltestlab.order.domain.vo.OrderLines;
import com.dochiri.tooltestlab.order.domain.vo.Phone;
import com.dochiri.tooltestlab.order.domain.vo.ProductName;
import com.dochiri.tooltestlab.order.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.RecipientName;
import com.dochiri.tooltestlab.order.domain.vo.ShippingAddress;
import com.dochiri.tooltestlab.order.domain.vo.StreetAddress;
import com.dochiri.tooltestlab.order.domain.vo.ZipCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    @Test
    @DisplayName("주문 UUID v7과 배송지 및 항목을 복원하고 변경 상태를 매핑한다")
    void mapsAndUpdatesOrder() {
        // given
        final Order order = Order.create(
                MemberReferenceId.parse("01900000-0000-7000-8000-000000000001"),
                OrderLines.of(OrderLine.of(ProductReferenceId.parse("01900000-0000-7000-8000-000000000002"),
                        ProductName.of("상품"), Quantity.of(2), Money.of(100))),
                ShippingAddress.of(RecipientName.of("홍길동"), Phone.of("010-1234-5678"),
                        StreetAddress.of("서울시 주소"), ZipCode.of("12345")));

        // when
        final OrderEntity entity = OrderMapper.toEntity(order);
        final var itemEntities = OrderMapper.toItemEntities(order, order.id().value());
        final Order restored = OrderMapper.toDomain(entity, itemEntities);
        OrderMapper.updateEntity(entity, order.pay());
        final Order updated = OrderMapper.toDomain(entity, itemEntities);

        // then
        assertEquals(7, restored.id().orderId().version());
        assertEquals(order.id(), restored.id());
        assertEquals(order.memberId(), restored.memberId());
        assertEquals(order.items(), restored.items());
        assertEquals(order.shippingAddress(), restored.shippingAddress());
        assertEquals(Money.of(200), restored.totalAmount());
        assertEquals(OrderStatus.PENDING, restored.status());
        assertEquals(OrderStatus.PAID, updated.status());
        assertEquals(order.id(), updated.id());
        assertEquals(order.shippingAddress(), updated.shippingAddress());
        assertEquals(OrderStatus.PENDING, order.status());
        assertEquals(36, entity.getOrderId().length());
        assertEquals(7, UUID.fromString(itemEntities.getFirst().getOrderLineId()).version());
    }
}
