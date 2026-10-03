package com.dochiri.tooltestlab.order.application.port.out;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.ProductName;
import com.dochiri.tooltestlab.order.domain.vo.ProductReferenceId;

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
