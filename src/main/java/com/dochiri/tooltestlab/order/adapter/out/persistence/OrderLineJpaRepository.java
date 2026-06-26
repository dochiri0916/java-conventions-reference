package com.dochiri.tooltestlab.order.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

interface OrderLineJpaRepository extends JpaRepository<OrderLineEntity, Long> {

    List<OrderLineEntity> findByOrderIdOrderById(Long orderId);

    List<OrderLineEntity> findByOrderIdIn(Collection<Long> orderIds);

    void deleteByOrderId(Long orderId);
}
