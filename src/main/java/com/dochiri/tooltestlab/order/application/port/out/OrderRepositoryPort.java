package com.dochiri.tooltestlab.order.application.port.out;

import com.dochiri.tooltestlab.order.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * 주문 저장소 Port (Application 소유).
 */
public interface OrderRepositoryPort {
    Order save(Order order);

    Optional<Order> findByPublicId(String publicId);

    List<Order> findByMemberId(String memberPublicId);
}
