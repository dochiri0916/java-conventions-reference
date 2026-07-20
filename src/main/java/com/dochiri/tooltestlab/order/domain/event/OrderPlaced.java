package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.OrderPlacedItems;
import com.dochiri.tooltestlab.order.domain.model.Quantity;

import static java.util.Objects.requireNonNull;

public record OrderPlaced(
        OrderId orderId,
        MemberId memberId,
        OrderPlacedItems items) {

    public OrderPlaced {
        if (orderId == null) {
            throw OrderDomainException.orderIdRequired();
        }
        if (memberId == null) {
            requireNonNull(memberId, "회원 ID는 null일 수 없습니다.");
        }
        if (items == null) {
            throw OrderDomainException.orderLinesRequired();
        }
    }

    public record ItemDetail(ProductId productId, ProductName productName, Quantity quantity, Money unitPrice) {
    }
}
