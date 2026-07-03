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

/**
 * 동시성 안전 모드용 OrderPersistenceAdapter.
 * {@code tool-test-lab.concurrency.safe=true}일 때 활성화.
 * PESSIMISTIC_WRITE 락으로 동시 수정을 직렬화한다.
 */
@Repository
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "true")
@RequiredArgsConstructor
class SafeOrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderLineJpaRepository orderLineJpaRepository;

    @Override
    public Order save(final Order order) {
        final Optional<OrderEntity> existing = orderJpaRepository.findByOrderIdForUpdate(order.id().value());
        if (existing.isPresent()) {
            final OrderEntity entity = existing.get();
            entity.apply(order);
            final OrderEntity savedOrder = orderJpaRepository.save(entity);
            orderLineJpaRepository.deleteByOrderId(savedOrder.getOrderId());
            final List<OrderLineEntity> items = OrderMapper.toItemEntities(order, savedOrder.getOrderId());
            orderLineJpaRepository.saveAll(items);
            return OrderMapper.toDomain(savedOrder, items);
        }

        final OrderEntity entity = OrderMapper.toEntity(order);
        final OrderEntity savedOrder = orderJpaRepository.save(entity);
        orderLineJpaRepository.deleteByOrderId(savedOrder.getOrderId());
        final List<OrderLineEntity> items = OrderMapper.toItemEntities(order, savedOrder.getOrderId());
        orderLineJpaRepository.saveAll(items);
        return OrderMapper.toDomain(savedOrder, items);
    }

    @Override
    public Optional<Order> findByPublicId(final String publicId) {
        return orderJpaRepository.findByOrderId(publicId)
                .map(entity -> {
                    final List<OrderLineEntity> items =
                            orderLineJpaRepository.findByOrderIdOrderById(entity.getOrderId());
                    return OrderMapper.toDomain(entity, items);
                });
    }

    @Override
    public List<Order> findByMemberId(final String memberPublicId) {
        final List<OrderEntity> entities = orderJpaRepository.findByMemberIdOrderByIdDesc(memberPublicId);

        final List<String> orderIds = entities.stream().map(OrderEntity::getOrderId).toList();
        Map<String, List<OrderLineEntity>> itemsByOrderId = orderLineJpaRepository.findByOrderIdIn(orderIds)
                .stream()
                .collect(groupingBy(OrderLineEntity::getOrderId));

        return entities.stream()
                .map(e -> OrderMapper.toDomain(e, itemsByOrderId.getOrDefault(e.getOrderId(), List.of())))
                .toList();
    }
}
