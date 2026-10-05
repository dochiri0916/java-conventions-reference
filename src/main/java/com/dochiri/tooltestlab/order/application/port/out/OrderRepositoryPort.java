package com.dochiri.tooltestlab.order.application.port.out;

import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.vo.OrderId;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;

import java.util.Optional;

/**
 * 주문 저장소 Port (Application 소유).
 * <p>
 * 기술적 저장소 작업 실패는 {@link RepositoryOperationException}으로 전달한다.
 */
public interface OrderRepositoryPort {
    Order create(Order order);

    Order update(Order order);

    Optional<Order> findByOrderId(OrderId orderId);

    Optional<Order> findByOrderIdForUpdate(OrderId orderId);

    Orders findByMemberId(MemberReferenceId memberId);
}
