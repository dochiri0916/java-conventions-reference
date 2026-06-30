package com.dochiri.tooltestlab.order.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

interface OrderLineJpaRepository extends JpaRepository<OrderLineEntity, Long> {

    List<OrderLineEntity> findByOrderIdOrderById(String orderId);

    List<OrderLineEntity> findByOrderIdIn(Collection<String> orderIds);

    void deleteByOrderId(String orderId);
}
