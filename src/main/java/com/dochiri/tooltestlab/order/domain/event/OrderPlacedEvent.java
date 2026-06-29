package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.shared.model.Money;

import java.util.List;

public record OrderPlacedEvent(
        OrderId orderId,
        MemberId memberId,
        List<ItemDetail> items) {

    public record ItemDetail(ProductId productId, ProductName productName, Quantity quantity, Money unitPrice) {
    }
}
