package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SafeOrderPersistenceAdapterTest {

    @Mock
    private OrderJpaRepository orderJpaRepository;

    @Mock
    private OrderLineJpaRepository orderLineJpaRepository;

    private SafeOrderPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SafeOrderPersistenceAdapter(orderJpaRepository, orderLineJpaRepository);
    }

    @Test
    @DisplayName("안전 주문 저장은 기존 주문이 없으면 새 엔티티를 저장한다")
    void saveCreatesNewOrderWhenMissing() {
        // given - 기존 주문이 조회되지 않는다
        Order order = order();
        when(orderJpaRepository.findByOrderIdForUpdate(order.id().value())).thenReturn(Optional.empty());
        when(orderJpaRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderLineJpaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // when - 안전 어댑터로 주문을 저장한다
        Order saved = adapter.create(order);

        // then - 새 주문과 주문 라인이 저장된다
        assertThat(saved.id()).isEqualTo(order.id());
        assertThat(saved.items().values()).hasSize(1);
        verify(orderLineJpaRepository).deleteByOrderId(order.id().value());
    }

    @Test
    @DisplayName("안전 주문 저장은 기존 주문이 있으면 엔티티 상태를 갱신한다")
    void saveUpdatesExistingOrderWhenPresent() {
        // given - 기존 주문이 비관적 락으로 조회된다
        Order pendingOrder = order();
        Order paidOrder = pendingOrder.pay();
        OrderEntity existingEntity = OrderMapper.toEntity(pendingOrder);
        when(orderJpaRepository.findByOrderIdForUpdate(paidOrder.id().value()))
                .thenReturn(Optional.of(existingEntity));
        when(orderJpaRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderLineJpaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // when - 변경된 주문 상태를 저장한다
        Order saved = adapter.update(paidOrder);

        // then - 기존 엔티티에 변경된 상태가 반영된다
        assertThat(saved.id()).isEqualTo(paidOrder.id());
        assertThat(saved.status()).isEqualTo(OrderStatus.PAID);
        verify(orderLineJpaRepository).deleteByOrderId(paidOrder.id().value());
    }

    private static Order order() {
        return Order.create(
                new MemberId("safeOrderMember"),
                OrderLines.fromArray(new OrderLine(
                        new ProductId("safeOrderProduct"),
                        new ProductName("안전 주문 상품"),
                        Quantity.from(2),
                        new Money(1_000))),
                new ShippingAddress(
                        new RecipientName("홍길동"),
                        new Phone("010-0000-0000"),
                        new StreetAddress("서울시 강남구"),
                        new ZipCode("12345")));
    }
}
