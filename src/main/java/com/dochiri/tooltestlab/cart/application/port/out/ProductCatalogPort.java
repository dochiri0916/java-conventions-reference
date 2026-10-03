package com.dochiri.tooltestlab.cart.application.port.out;

import com.dochiri.tooltestlab.cart.domain.model.ProductName;
import com.dochiri.tooltestlab.cart.domain.model.ProductReferenceId;
import com.dochiri.tooltestlab.global.domain.Money;

/**
 * 상품 정보 조회 Port (Application 소유). 구현은 Adapter가 담당한다.
 */
public interface ProductCatalogPort {

    ProductSnapshot findProduct(ProductReferenceId productId);

    record ProductSnapshot(ProductReferenceId id, ProductName name, Money price) {
    }
}
