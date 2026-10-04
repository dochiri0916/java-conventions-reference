package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;
import static java.util.stream.Collectors.groupingBy;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderLineJpaRepository orderLineJpaRepository;

    @Override
    public Order create(final Order order) {
        return execute(() -> {
            final OrderEntity entity = OrderMapper.toEntity(order);
            final OrderEntity savedOrder = orderJpaRepository.save(entity);
            orderLineJpaRepository.deleteByOrderId(order.id().value());
            final List<OrderLineEntity> items = OrderMapper.toItemEntities(order, order.id().value());
            orderLineJpaRepository.saveAll(items);
            return OrderMapper.toDomain(savedOrder, items);
        });
    }

    @Override
    public Order update(final Order order) {
        return execute(() -> {
            final OrderEntity entity = orderJpaRepository.findByOrderId(order.id().value()).orElseThrow();
            OrderMapper.updateEntity(entity, order);
            final OrderEntity savedOrder = orderJpaRepository.save(entity);
            orderLineJpaRepository.deleteByOrderId(order.id().value());
            final List<OrderLineEntity> items = OrderMapper.toItemEntities(order, order.id().value());
            orderLineJpaRepository.saveAll(items);
            return OrderMapper.toDomain(savedOrder, items);
        });
    }

    @Override
    public Optional<Order> findByPublicIdForUpdate(final String publicId) {
        return execute(() -> orderJpaRepository.findByOrderIdForUpdate(publicId).map(entity ->
                OrderMapper.toDomain(entity, orderLineJpaRepository.findByOrderIdOrderById(entity.getOrderId()))));
    }

    @Override
    public Optional<Order> findByPublicId(final String publicId) {
        return execute(() -> orderJpaRepository.findByOrderId(publicId)
                .map(entity -> {
                    final List<OrderLineEntity> items =
                            orderLineJpaRepository.findByOrderIdOrderById(entity.getOrderId());
                    return OrderMapper.toDomain(entity, items);
                }));
    }

    @Override
    public List<Order> findByMemberId(final String memberPublicId) {
        return execute(() -> {
            final List<OrderEntity> entities = orderJpaRepository.findByMemberIdOrderByIdDesc(memberPublicId);

            // batch load items
            final List<String> orderIds = entities.stream().map(OrderEntity::getOrderId).toList();
            final Map<String, List<OrderLineEntity>> itemsByOrderId = orderLineJpaRepository.findByOrderIdIn(orderIds)
                    .stream()
                    .collect(groupingBy(OrderLineEntity::getOrderId));

            return entities.stream()
                    .map(e -> OrderMapper.toDomain(e, itemsByOrderId.getOrDefault(e.getOrderId(), List.of())))
                    .toList();
        });
    }
}
