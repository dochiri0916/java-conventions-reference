package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.application.port.out.Orders;
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

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
final class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderLineJpaRepository orderLineJpaRepository;

    @Override
    public Order create(final Order order) {
        return execute(() -> {
            OrderEntity entity = OrderMapper.toEntity(order);
            final OrderEntity savedOrder = orderJpaRepository.saveAndFlush(entity);
            orderLineJpaRepository.deleteByOrderId(
                    order.id()
                            .value());
            orderLineJpaRepository.flush();
            final List<OrderLineEntity> items = OrderMapper.toItemEntities(order,
                    order.id()
                            .value());
            orderLineJpaRepository.saveAllAndFlush(items);
            return OrderMapper.toDomain(savedOrder, items);
        });
    }

    @Override
    public Order update(final Order order) {
        return execute(() -> {
            OrderEntity entity = orderJpaRepository.findByPublicId(
                    order.id()
                            .value())
                            .orElseThrow();
            OrderMapper.updateEntity(entity, order);
            final OrderEntity savedOrder = orderJpaRepository.saveAndFlush(entity);
            orderLineJpaRepository.deleteByOrderId(
                    order.id()
                            .value());
            orderLineJpaRepository.flush();
            final List<OrderLineEntity> items = OrderMapper.toItemEntities(order,
                    order.id()
                            .value());
            orderLineJpaRepository.saveAllAndFlush(items);
            return OrderMapper.toDomain(savedOrder, items);
        });
    }

    @Override
    public Optional<Order> findByOrderIdForUpdate(final OrderId orderId) {
        return execute(() -> orderJpaRepository.findByPublicIdForUpdate(
                orderId.value())
                        .map(entity ->
                OrderMapper.toDomain(entity,
                orderLineJpaRepository.findByOrderIdOrderById(
                        entity.getPublicId()))));
    }

    @Override
    public Optional<Order> findByOrderId(final OrderId orderId) {
        return execute(() -> orderJpaRepository.findByPublicId(
                orderId.value())
                .map(entity -> {
                    List<OrderLineEntity> items =
                            orderLineJpaRepository.findByOrderIdOrderById(
                                    entity.getPublicId());
                    return OrderMapper.toDomain(entity, items);
                }));
    }

    @Override
    public Orders findByMemberId(final MemberReferenceId memberId) {
        final String value = memberId.value();
        return execute(() -> toOrders(orderJpaRepository.findByMemberIdOrderByIdDesc(value)));
    }

    private Orders toOrders(final List<OrderEntity> entities) {
        final List<String> orderIds = entities.stream()
                .map(OrderEntity::getPublicId)
                .toList();
        final Map<String, List<OrderLineEntity>> itemsByOrderId = orderLineJpaRepository.findByOrderIdIn(orderIds)
                .stream()
                .collect(groupingBy(OrderLineEntity::getOrderId));
        final List<Order> orders = entities.stream()
                .map(entity -> OrderMapper.toDomain(entity, itemsByOrderId.getOrDefault(entity.getPublicId(), List.of())))
                .toList();
        return new Orders(orders);
    }
}
