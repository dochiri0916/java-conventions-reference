package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.global.domain.Money;
import com.dochiri.tooltestlab.global.domain.Quantity;
import com.dochiri.tooltestlab.order.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.ProductNameRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.QuantityRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.UnitPriceRequiredException;

public record OrderLine(
        ProductReferenceId productId,
        ProductName productName,
        Quantity quantity,
        Money unitPrice
) {

    public OrderLine {
        if (productId == null) {
            throw new ProductReferenceIdRequiredException();
        }
        if (productName == null) {
            throw new ProductNameRequiredException();
        }
        if (quantity == null) {
            throw new QuantityRequiredException();
        }
        if (unitPrice == null) {
            throw new UnitPriceRequiredException();
        }
    }

    public Money totalPrice() {
        return unitPrice.multiply(quantity.value());
    }
}
