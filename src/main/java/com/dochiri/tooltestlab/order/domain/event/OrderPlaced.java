package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.OrderPlacedItems;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.product.domain.model.Money;

import static java.util.Objects.requireNonNull;

public record OrderPlaced(
        OrderId orderId,
        MemberId memberId,
        OrderPlacedItems items) {

    public OrderPlaced {
        requireNonNull(orderId, "주문 ID는 null일 수 없습니다.");
        requireNonNull(memberId, "회원 ID는 null일 수 없습니다.");
        requireNonNull(items, "주문 상품 목록은 null일 수 없습니다.");
    }

    public record ItemDetail(ProductId productId, ProductName productName, Quantity quantity, Money unitPrice) {
    }
}
