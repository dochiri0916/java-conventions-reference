package com.dochiri.tooltestlab.order.application.port.out;

import com.dochiri.tooltestlab.global.domain.Money;
import com.dochiri.tooltestlab.global.domain.Quantity;
import com.dochiri.tooltestlab.order.domain.model.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.model.ProductName;
import com.dochiri.tooltestlab.order.domain.model.ProductReferenceId;

import java.util.List;

/**
 * 장바구니 연동 Port (Application 소유). 구현은 Adapter가 담당한다.
 */
public interface CartPort {

    List<CartLine> findLines(MemberReferenceId memberId);

    void clear(MemberReferenceId memberId);

    record CartLine(ProductReferenceId productId, ProductName productName, Quantity quantity, Money unitPrice) {
    }
}
