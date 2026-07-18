package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidQuantityException;
import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;

public record OrderLine(
        ProductId productId,
        ProductName productName,
        Quantity quantity,
        Money unitPrice
) {

    public OrderLine {
        if (productId == null) {
            throw InvalidProductIdException.required();
        }
        if (productName == null) {
            throw InvalidProductNameException.required();
        }
        if (quantity == null) {
            throw InvalidQuantityException.required();
        }
        if (unitPrice == null) {
            throw InvalidMoneyException.required();
        }
    }

    public Money totalPrice() {
        return unitPrice.multiply(quantity.value());
    }
}
