package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Money;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;

import java.util.List;

/**
 * 주문이 생성되면 발행되는 도메인 이벤트.
 * 재고 차감, 장바구니 비우기 등의 후속 처리를 트리거한다.
 */
public record OrderPlacedEvent(
        OrderId orderId,
        MemberId memberId,
        List<ItemDetail> items) {

    public record ItemDetail(ProductId productId, ProductName productName, Quantity quantity, Money unitPrice) {
    }
}
