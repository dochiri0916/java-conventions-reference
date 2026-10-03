package com.dochiri.tooltestlab.order.application.port.out;

import com.dochiri.tooltestlab.global.domain.Quantity;
import com.dochiri.tooltestlab.order.domain.model.ProductReferenceId;

/**
 * 상품 재고 연동 Port (Application 소유). 구현은 Adapter가 담당한다.
 */
public interface ProductStockPort {

    void decrease(ProductReferenceId productId, Quantity quantity);

    void increase(ProductReferenceId productId, Quantity quantity);
}
