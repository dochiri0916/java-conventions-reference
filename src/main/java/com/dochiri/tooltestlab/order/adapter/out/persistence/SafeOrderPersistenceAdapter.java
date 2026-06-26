package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.model.Order;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

/**
 * 동시성 안전 모드용 OrderPersistenceAdapter.
 * {@code tool-test-lab.concurrency.safe=true}일 때 활성화.
 * PESSIMISTIC_WRITE 락으로 동시 수정을 직렬화한다.
 */
@Repository
@Transactional(readOnly = true)
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "true")
class SafeOrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderLineJpaRepository orderLineJpaRepository;

    SafeOrderPersistenceAdapter(OrderJpaRepository orderJpaRepository, OrderLineJpaRepository orderLineJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderLineJpaRepository = orderLineJpaRepository;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        // PESSIMISTIC_WRITE로 락을 잡고 조회 → 동시 수정 방지
        Optional<OrderEntity> existing = orderJpaRepository.findByPublicIdForUpdate(order.id().value());
        OrderEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            entity.apply(order);
        } else {
            entity = OrderMapper.toEntity(order);
        }
        OrderEntity savedOrder = orderJpaRepository.save(entity);

        orderLineJpaRepository.deleteByOrderId(savedOrder.getId());
        List<OrderLineEntity> items = OrderMapper.toItemEntities(order, savedOrder);
        orderLineJpaRepository.saveAll(items);

        return OrderMapper.toDomain(savedOrder, items);
    }

    @Override
    public Optional<Order> findByPublicId(String publicId) {
        return orderJpaRepository.findByPublicId(publicId)
                .map(entity -> {
                    List<OrderLineEntity> items = orderLineJpaRepository.findByOrderIdOrderById(entity.getId());
                    return OrderMapper.toDomain(entity, items);
                });
    }

    @Override
    public List<Order> findByMemberId(String memberPublicId) {
        List<OrderEntity> entities = orderJpaRepository.findByMemberPublicIdOrderByIdDesc(memberPublicId);

        List<Long> orderIds = entities.stream().map(OrderEntity::getId).toList();
        Map<Long, List<OrderLineEntity>> itemsByOrderId = orderLineJpaRepository.findByOrderIdIn(orderIds)
                .stream()
                .collect(groupingBy(OrderLineEntity::getOrderId));

        return entities.stream()
                .map(e -> OrderMapper.toDomain(e, itemsByOrderId.getOrDefault(e.getId(), List.of())))
                .toList();
    }
}
