package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;

public record OrderLine(
        ProductId productId,
        ProductName productName,
        Quantity quantity,
        Money unitPrice
) {

    public OrderLine {
        if (productId == null) {
            throw OrderDomainException.productIdRequired();
        }
        if (productName == null) {
            throw OrderDomainException.productNameRequired();
        }
        if (quantity == null) {
            throw OrderDomainException.quantityRequired();
        }
        if (unitPrice == null) {
            throw OrderDomainException.unitPriceRequired();
        }
    }

    public Money totalPrice() {
        return unitPrice.multiply(quantity.value());
    }
}
