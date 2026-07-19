package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.OrderPlacedItems;
import com.dochiri.tooltestlab.order.domain.model.Quantity;

public record OrderPlaced(
        OrderId orderId,
        MemberId memberId,
        OrderPlacedItems items) {

    public OrderPlaced {
        if (orderId == null) {
            throw InvalidOrderIdException.required();
        }
        if (memberId == null) {
            throw InvalidMemberIdException.required();
        }
        if (items == null) {
            throw InvalidOrderLinesException.required();
        }
    }

    public record ItemDetail(ProductId productId, ProductName productName, Quantity quantity, Money unitPrice) {
    }
}
