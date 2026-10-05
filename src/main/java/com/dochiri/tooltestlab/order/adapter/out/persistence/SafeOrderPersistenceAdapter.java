package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.vo.OrderId;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;
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
    public Order create(final Order order) {
        return execute(() -> {
            final OrderEntity entity = OrderMapper.toEntity(order);
            final OrderEntity savedOrder = orderJpaRepository.saveAndFlush(entity);
            orderLineJpaRepository.deleteByOrderId(savedOrder.getPublicId());
            final List<OrderLineEntity> items = OrderMapper.toItemEntities(order, savedOrder.getPublicId());
            orderLineJpaRepository.saveAllAndFlush(items);
            return OrderMapper.toDomain(savedOrder, items);
        });
    }

    @Override
    public Order update(final Order order) {
        return execute(() -> {
            final OrderEntity entity = orderJpaRepository.findByPublicIdForUpdate(order.id().value()).orElseThrow();
            OrderMapper.updateEntity(entity, order);
            final OrderEntity savedOrder = orderJpaRepository.saveAndFlush(entity);
            orderLineJpaRepository.deleteByOrderId(savedOrder.getPublicId());
            final List<OrderLineEntity> items = OrderMapper.toItemEntities(order, savedOrder.getPublicId());
            orderLineJpaRepository.saveAllAndFlush(items);
            return OrderMapper.toDomain(savedOrder, items);
        });
    }

    @Override
    public Optional<Order> findByOrderIdForUpdate(final OrderId orderId) {
        return execute(() -> orderJpaRepository.findByPublicIdForUpdate(orderId.value()).map(entity ->
                OrderMapper.toDomain(entity, orderLineJpaRepository.findByOrderIdOrderById(entity.getPublicId()))));
    }

    @Override
    public Optional<Order> findByOrderId(final OrderId orderId) {
        return execute(() -> orderJpaRepository.findByPublicId(orderId.value())
                .map(entity -> {
                    final List<OrderLineEntity> items =
                            orderLineJpaRepository.findByOrderIdOrderById(entity.getPublicId());
                    return OrderMapper.toDomain(entity, items);
                }));
    }

    @Override
    public List<Order> findByMemberId(final MemberReferenceId memberId) {
        return execute(() -> {
            final List<OrderEntity> entities = orderJpaRepository.findByMemberIdOrderByIdDesc(memberId.value());

            final List<String> orderIds = entities.stream().map(OrderEntity::getPublicId).toList();
            final Map<String, List<OrderLineEntity>> itemsByOrderId = orderLineJpaRepository.findByOrderIdIn(orderIds)
                    .stream()
                    .collect(groupingBy(OrderLineEntity::getOrderId));

            return entities.stream()
                    .map(e -> OrderMapper.toDomain(e, itemsByOrderId.getOrDefault(e.getPublicId(), List.of())))
                    .toList();
        });
    }
}
