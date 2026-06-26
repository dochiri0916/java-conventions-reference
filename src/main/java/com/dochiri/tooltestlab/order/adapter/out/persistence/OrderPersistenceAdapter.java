package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderLineJpaRepository orderLineJpaRepository;

    @Override
    public Order save(Order order) {
        Optional<OrderEntity> existing = orderJpaRepository.findByPublicId(order.id().value());
        OrderEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            entity.apply(order);
        } else {
            entity = OrderMapper.toEntity(order);
        }
        OrderEntity savedOrder = orderJpaRepository.save(entity);

        // items: 기존 삭제 후 새로 저장 (orphanRemoval 수동 구현)
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

        // batch load items
        List<Long> orderIds = entities.stream().map(OrderEntity::getId).toList();
        Map<Long, List<OrderLineEntity>> itemsByOrderId = orderLineJpaRepository.findByOrderIdIn(orderIds)
                .stream()
                .collect(groupingBy(OrderLineEntity::getOrderId));

        return entities.stream()
                .map(e -> OrderMapper.toDomain(e, itemsByOrderId.getOrDefault(e.getId(), List.of())))
                .toList();
    }
}
